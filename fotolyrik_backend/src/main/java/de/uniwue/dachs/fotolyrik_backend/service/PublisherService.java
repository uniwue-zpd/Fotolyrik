package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PublisherPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import de.uniwue.dachs.fotolyrik_backend.repository.PublisherRepository;
import de.uniwue.dachs.fotolyrik_backend.specification.PublisherSearchCriteria;
import de.uniwue.dachs.fotolyrik_backend.specification.PublisherSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublisherMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    /**
     * @return a {@link List} of available {@link Publisher} as {@link PublisherDTO} objects
     */
    public List<PublisherDTO> getAllPublishers() {
        return publisherMapper.PublishersToPublisherDTOs(publisherRepository.findAll());
    }

    /**
     * GET a page of publishers matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param pageable paging and sorting information
     * @return a {@link Page} of matching {@link Publisher} as {@link PublisherDTO} objects
     */
    public Page<PublisherDTO> searchPaginated(PublisherSearchCriteria criteria, Pageable pageable) {
        return publisherRepository.findAll(buildSpecification(criteria), pageable)
                .map(publisherMapper::PublisherToPublisherDTO);
    }

    /**
     * GET an unpaginated list of publisher previews matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param sort sort order; falls back to ascending name if unsorted
     * @return a {@link List} of matching {@link Publisher} as {@link PublisherPreviewDTO} objects
     */
    public List<PublisherPreviewDTO> searchFlat(PublisherSearchCriteria criteria, Sort sort) {
        Sort effectiveSort = sort != null && sort.isSorted() ? sort : Sort.by("name").ascending();
        return publisherMapper.PublishersToPublisherPreviewDTOs(
                publisherRepository.findAll(buildSpecification(criteria), effectiveSort));
    }

    /**
     * Builds a {@link Specification} that combines all set criteria with AND.
     * @param c search criteria; {@code null} values and blank strings are ignored
     */
    private Specification<Publisher> buildSpecification(PublisherSearchCriteria c) {
        Specification<Publisher> spec = Specification.where(null);
        if (c == null) return spec;
        if (hasText(c.name())) spec = spec.and(PublisherSpecification.hasName(c.name()));
        return spec;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * @param id ID of the {@link Publisher} to be found
     * @return {@link PublisherDTO} of existing {@link Publisher} entry
     */
    public Optional<PublisherDTO> getPublisherById(Long id) {
        return publisherRepository.findById(id).map(publisherMapper::PublisherToPublisherDTO);
    }

    /**
     * @param publisherDTO {@link PublisherDTO} object to be created
     * @return {@link PublisherDTO} of created {@link Publisher} object
     */
    @Transactional
    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        var entity = publisherMapper.PublisherDTOToPublisher(publisherDTO);
        var savedEntity = publisherRepository.save(entity);
        return publisherMapper.PublisherToPublisherDTO(savedEntity);
    }

    /**
     * @param id ID of the {@link Publisher} object to be updated
     * @param publisher {@link PublisherDTO} object
     * @return {@link PublisherDTO} of the updated {@link Publisher} object
     */
    @Transactional
    public PublisherDTO updatePublisher(Long id, PublisherDTO publisher) {
        return publisherRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(publisher);
                    entity.setName(publisher.getName());
                    entity.setDescription(publisher.getDescription());
                    return publisherRepository.save(entity);
                }).map(publisherMapper::PublisherToPublisherDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id ID of the {@link Publisher} object to be deleted
     */
    @Transactional
    public void deletePublisherById(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        }
        publisherRepository.deleteById(id);
    }
}
