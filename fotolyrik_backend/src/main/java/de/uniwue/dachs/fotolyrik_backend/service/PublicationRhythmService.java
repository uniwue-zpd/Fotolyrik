package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublicationRhythmDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import de.uniwue.dachs.fotolyrik_backend.repository.PublicationRhythmRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublicationRhythmMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationRhythmService {
    private final PublicationRhythmRepository publicationRhythmRepository;
    private final PublicationRhythmMapper publicationRhythmMapper;

    public PublicationRhythmService(PublicationRhythmRepository publicationRhythmRepository, PublicationRhythmMapper publicationRhythmMapper) {
        this.publicationRhythmRepository = publicationRhythmRepository;
        this.publicationRhythmMapper = publicationRhythmMapper;
    }

    /**
     * @return a {@link List} of available {@link PublicationRhythm} as {@link PublicationRhythmDTO} objects
     */
    public List<PublicationRhythmDTO> getAllPublicationRhythms() {
        return publicationRhythmMapper.PublicationRhythmsToPublicationRhythmDTOs(publicationRhythmRepository.findAll());
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be found
     * @return {@link PublicationRhythmDTO} of existing {@link PublicationRhythm} object
     */
    public Optional<PublicationRhythmDTO> getPublicationRhythmById(Long id) {
        return publicationRhythmRepository.findById(id).map(publicationRhythmMapper::PublicationRhythmToPublicationRhythmDTO);
    }

    /**
     * @param publicationRhythmDTO {@link PublicationRhythmDTO} object to be created
     * @return created {@link PublicationRhythm} object
     */
    public PublicationRhythmDTO createPublicationRhythm(PublicationRhythmDTO publicationRhythmDTO) {
        var entity = publicationRhythmMapper.PublicationRhythmDTOToPublicationRhythm(publicationRhythmDTO);
        var savedEntity = publicationRhythmRepository.save(entity);
        return publicationRhythmMapper.PublicationRhythmToPublicationRhythmDTO(savedEntity);
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be updated
     * @param publicationRhythmDTO {@link PublicationRhythmDTO} object
     * @return {@link PublicationRhythmDTO} of the updated {@link PublicationRhythm} object
     */
    public PublicationRhythmDTO updatePublicationRhythm(Long id, PublicationRhythmDTO publicationRhythmDTO) {
        return publicationRhythmRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(publicationRhythmDTO);
                    entity.setValue(publicationRhythmDTO.getValue());
                    entity.setDescription(publicationRhythmDTO.getDescription());
                    return publicationRhythmRepository.save(entity);
                }).map(publicationRhythmMapper::PublicationRhythmToPublicationRhythmDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be deleted
     */
    public void deletePublicationRhythmById(Long id) {
        if (!publicationRhythmRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        }
        publicationRhythmRepository.deleteById(id);
    }
}
