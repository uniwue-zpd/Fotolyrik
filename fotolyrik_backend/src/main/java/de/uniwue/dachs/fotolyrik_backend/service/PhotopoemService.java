package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.ContributionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PhotopoemPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.*;
import de.uniwue.dachs.fotolyrik_backend.repository.*;
import de.uniwue.dachs.fotolyrik_backend.specification.PhotopoemSearchCriteria;
import de.uniwue.dachs.fotolyrik_backend.specification.PhotopoemSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.helper.PhotopoemHighlightPicker;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotopoemService {
    private final PhotopoemRepository photopoemRepository;
    private final FullTextService fullTextService;
    private final PhotopoemMapper photopoemMapper;
    private final PhotopoemHighlightPicker photopoemHighlightPicker;
    private final ContributionMapper contributionMapper;
    private final PersonRepository personRepository;

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            FullTextService fullTextService,
                            PhotopoemMapper photopoemMapper,
                            PhotopoemHighlightPicker photopoemHighlightPicker,
                            PersonRepository personRepository,
                            ContributionMapper contributionMapper) {
        this.photopoemRepository = photopoemRepository;
        this.fullTextService = fullTextService;
        this.photopoemMapper = photopoemMapper;
        this.photopoemHighlightPicker = photopoemHighlightPicker;
        this.contributionMapper = contributionMapper;
        this.personRepository = personRepository;
    }

    /**
     * Returns a paginated list of photopoems matching the given criteria.
     * @param criteria optional filters; unset values are ignored
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of full {@link PhotopoemDTO} objects
     */
    public Page<PhotopoemDTO> searchPaginated(PhotopoemSearchCriteria criteria, Pageable pageable) {
        return photopoemRepository.findAll(buildSpecification(criteria), pageable)
                .map(photopoemMapper::PhotopoemToPhotopoemDTO);
    }

    /**
     * Returns an unpaginated list of lightweight photopoem previews matching the given criteria.
     * @param criteria optional filters; unset values are ignored
     * @param sort sort order; falls back to ascending ID if unsorted
     * @return a {@link List} of {@link PhotopoemPreviewDTO} objects
     */
    public List<PhotopoemPreviewDTO> searchFlat(PhotopoemSearchCriteria criteria, Sort sort) {
        Sort effectiveSort = sort != null && sort.isSorted() ? sort : Sort.by("id");
        return photopoemRepository.findAll(buildSpecification(criteria), effectiveSort)
                .stream()
                .map(photopoemMapper::PhotopoemToPhotopoemPreviewDTO)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * GET all photopoems
     * @return {@link List} of all available {@link PhotopoemDTO} objects
     */
    public List<PhotopoemDTO> getAllPhotopoems() {
        return  photopoemRepository.findAll()
                .stream()
                .map(photopoemMapper::PhotopoemToPhotopoemDTO)
                .sorted(Comparator.comparing(PhotopoemDTO::getId)).toList();
    }

    /**
     * GET a photopoem object by its ID
     * @return {@link PhotopoemDTO}
     */
    public Optional<PhotopoemDTO> getPhotopoemById(Long id) {
        return photopoemRepository.findById(id).map(photopoemMapper::PhotopoemToPhotopoemDTO);
    }

    /**
     * GET a photopoem as the monthly highlight
     * @return an {@link Optional} containing the {@link PhotopoemDTO} of the monthly highlight, or an empty {@link Optional} if no photopoems are available
     */
    public Optional<PhotopoemDTO> getMonthlyHighlight() {
        List<Long> allIds = photopoemRepository.findAllIds();
        if (allIds.isEmpty()) return Optional.empty();
        int index = photopoemHighlightPicker.calculateMonthlyIndex(allIds.size());
        Long highlightId = allIds.get(index);
        return photopoemRepository.findById(highlightId).map(photopoemMapper::PhotopoemToPhotopoemDTO);
    }

    /**
     * @param photopoemDTO ({@link PhotopoemDTO})
     * @return a {@link Photopoem} object and makes it persistent
     */
    @Transactional
    public PhotopoemDTO createPhotopoem(PhotopoemDTO photopoemDTO) {
        Photopoem photopoem = photopoemMapper.PhotopoemDTOToPhotopoem(photopoemDTO);
        Photopoem createdPhotopoem = photopoemRepository.save(photopoem);

        // Update the pseudonyms of the person entities based on the contributions of the photopoem
        updatePersonPseudonymsFromContributions(photopoemDTO);

        return photopoemMapper.PhotopoemToPhotopoemDTO(createdPhotopoem);
    }

    /**
     * @param id of the {@link Photopoem} object
     * @param updatedPhotopoem is an {@link PhotopoemDTO} object
     * @return {@link Photopoem} object and persists the updates
     */
    @Transactional
    public PhotopoemDTO updatePhotopoem(Long id, PhotopoemDTO updatedPhotopoem) {
        return photopoemRepository.findById(id).map(photopoemToUpdate -> {
            photopoemToUpdate.updateBaseEntityNotes(updatedPhotopoem);
            photopoemMapper.updatePhotopoemFromDTO(photopoemToUpdate, updatedPhotopoem);

            // Update the pseudonyms of the person entities based on the contributions of the photopoem
            photopoemToUpdate.updateContributions(contributionMapper.DTOsToContributions(updatedPhotopoem.getContributions(), photopoemToUpdate));
            updatePersonPseudonymsFromContributions(updatedPhotopoem);

            Photopoem savedPhotopoem = photopoemRepository.save(photopoemToUpdate);
            return photopoemMapper.PhotopoemToPhotopoemDTO(savedPhotopoem);
        }).orElseThrow(() -> new EntityNotFoundException("Photopoem with id'" + id + "' can't be found"));
    }

    /**
     * @param id ID of the photopoem to be deleted
     */
    @Transactional
    public void deletePhotopoem(Long id) {
        if (!photopoemRepository.existsById(id)) {
            throw new EntityNotFoundException("Photopoem with id '" + id + "' does not exist");
        }
        fullTextService.deleteFullTextByPhotopoemID(id);
        photopoemRepository.deleteById(id);
    }

    /**
     * Builds a {@link Specification} that combines all set criteria with AND.
     * @param c search criteria; {@code null} values and empty strings are ignored
     */
    private Specification<Photopoem> buildSpecification(PhotopoemSearchCriteria c) {
        Specification<Photopoem> spec = Specification.where(null);
        if (c == null) return spec;
        if (hasText(c.title())) spec = spec.and(PhotopoemSpecification.hasTitle(c.title()));
        if (hasText(c.subtitle())) spec = spec.and(PhotopoemSpecification.hasSubtitle(c.subtitle()));
        if (hasText(c.altTitle())) spec = spec.and(PhotopoemSpecification.hasAltTitle(c.altTitle()));
        if (hasText(c.series())) spec = spec.and(PhotopoemSpecification.hasSeries(c.series()));
        if (c.volume() != null) spec = spec.and(PhotopoemSpecification.hasVolume(c.volume()));
        if (c.issue() != null) spec = spec.and(PhotopoemSpecification.hasIssue(c.issue()));
        if (hasText(c.publicationDate())) spec = spec.and(PhotopoemSpecification.hasPublicationDate(c.publicationDate()));
        if (c.pubMediumId() != null) spec = spec.and(PhotopoemSpecification.hasPubMediumId(c.pubMediumId()));
        if (hasText(c.pubMedium())) spec = spec.and(PhotopoemSpecification.hasPubMedium(c.pubMedium()));
        if (c.pubPlaceId() != null) spec = spec.and(PhotopoemSpecification.hasPubPlaceId(c.pubPlaceId()));
        if (c.locationId() != null) spec = spec.and(PhotopoemSpecification.hasLocationId(c.locationId()));
        if (c.authorId() != null) spec = spec.and(PhotopoemSpecification.hasAuthorId(c.authorId()));
        if (hasText(c.author())) spec = spec.and(PhotopoemSpecification.hasAuthor(c.author()));
        if (c.photographerId() != null) spec = spec.and(PhotopoemSpecification.hasPhotographerId(c.photographerId()));
        if (hasText(c.photographer())) spec = spec.and(PhotopoemSpecification.hasPhotographer(c.photographer()));
        if (c.depictedPersonId() != null) spec = spec.and(PhotopoemSpecification.hasDepictedPersonId(c.depictedPersonId()));
        if (hasText(c.depictedPerson())) spec = spec.and(PhotopoemSpecification.hasDepictedPerson(c.depictedPerson()));
        if (c.participantId() != null) spec = spec.and(PhotopoemSpecification.hasParticipantId(c.participantId()));
        if (hasText(c.participant())) spec = spec.and(PhotopoemSpecification.hasParticipant(c.participant()));
        if (c.otherContributorId() != null) spec = spec.and(PhotopoemSpecification.hasOtherContributorId(c.otherContributorId()));
        if (hasText(c.otherContributor())) spec = spec.and(PhotopoemSpecification.hasOtherContributor(c.otherContributor()));
        if (c.themeId() != null) spec = spec.and(PhotopoemSpecification.hasThemeId(c.themeId()));
        if (hasText(c.theme())) spec = spec.and(PhotopoemSpecification.hasTheme(c.theme()));
        if (c.imageMotifId() != null) spec = spec.and(PhotopoemSpecification.hasImageMotifId(c.imageMotifId()));
        if (hasText(c.imageMotif())) spec = spec.and(PhotopoemSpecification.hasImageMotif(c.imageMotif()));
        if (c.copyrightStatusImageId() != null) spec = spec.and(PhotopoemSpecification.hasCopyrightStatusImageId(c.copyrightStatusImageId()));
        if (hasText(c.copyrightStatusImage())) spec = spec.and(PhotopoemSpecification.hasCopyrightStatusImage(c.copyrightStatusImage()));
        if (c.copyrightStatusTextId() != null) spec = spec.and(PhotopoemSpecification.hasCopyrightStatusTextId(c.copyrightStatusTextId()));
        if (hasText(c.copyrightStatusText())) spec = spec.and(PhotopoemSpecification.hasCopyrightStatusText(c.copyrightStatusText()));
        if (c.languageId() != null) spec = spec.and(PhotopoemSpecification.hasLanguageId(c.languageId()));
        if (hasText(c.language())) spec = spec.and(PhotopoemSpecification.hasLanguage(c.language()));
        return spec;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * Performs an update of the pseudonyms field of some person entity based on the contribution of a photopoem.
     * It allows to keep track of the pseudonyms used by a person in different contributions and to update the pseudonyms field of the person entity accordingly.
     * @param photopoemDTO {@link PhotopoemDTO} object containing the contributions based on which the pseudonyms should be updated
     */
    private void updatePersonPseudonymsFromContributions(PhotopoemDTO photopoemDTO) {
        if (photopoemDTO == null || photopoemDTO.getContributions() == null) return;
        for (ContributionDTO contribution: photopoemDTO.getContributions()) {
            if (contribution == null) continue;
            var pseudonym = contribution.getPseudonym();
            var contributor = contribution.getContributor();
            if (pseudonym != null && !pseudonym.isBlank() && contributor != null && contributor.getId() != null) {
                personRepository.findById(contributor.getId()).ifPresent(person -> {
                    Set<String> cleanedPseudonyms = person.getPseudonyms().stream()
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .filter(s -> !s.isBlank())
                            .collect(Collectors.toSet());
                    if (!cleanedPseudonyms.contains(pseudonym.trim().toLowerCase())) {
                        person.getPseudonyms().add(pseudonym);
                        personRepository.save(person);
                    }
                });
            }
        }
    }
}
