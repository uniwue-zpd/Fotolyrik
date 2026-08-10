package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.ContributionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.model.*;
import de.uniwue.dachs.fotolyrik_backend.repository.*;
import de.uniwue.dachs.fotolyrik_backend.specification.PhotopoemSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.helper.PhotopoemHighlightPicker;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Returns a page object containing photopoems
     * @param pageable pagination parameters to be used
     * @return A {@link Page} of {@link PhotopoemDTO} objects
     */
    public Page<PhotopoemDTO> getPaginatedPhotopoems(Pageable pageable) {
        Page<Photopoem> photopoemPage = photopoemRepository.findAll(pageable);
        return photopoemPage.map(photopoemMapper::PhotopoemToPhotopoemDTO);
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
     * Filter photopoems based on various criteria.
     * @param title refers to the title of the photopoem
     * @param subtitle refers to the subtitle of the photopoem
     * @param altTitle refers to the alternative title of the photopoem
     * @param series refers to the series of the photopoem
     * @param volume refers to the volume number
     * @param issue refers to the issue number
     * @param publicationDate refers to the publication date
     * @param pubMediumId refers to the ID of the publication medium
     * @param pubMedium refers to the name of the publication medium
     * @param authorId refers to the ID of the author
     * @param author refers to the name of the author
     * @param photographerId refers to the ID of the photographer
     * @param photographer refers to the name of the photographer
     * @param depictedPersonId refers to the ID of the person depicted
     * @param depictedPerson refers to the name of the person depicted
     * @param participantId refers to the ID of the participant
     * @param participant refers to the name of the participant
     * @param contributorId refers to the ID of other contributors
     * @param otherContributor refers to the name of other contributors
     * @param themeId refers to the ID of the theme
     * @param theme refers to the name of the theme
     * @param imageMotifId refers to the ID of the image motif
     * @param imageMotif refers to the name of the image motif
     * @param copyrightStatusImageId refers to the ID of the copyright status for the image
     * @param copyrightStatusImage refers to the name of the copyright status for the image
     * @param copyrightStatusTextId refers to the ID of the copyright status for the text
     * @param copyrightStatusText refers to the name of the copyright status for the text
     * @param languageId refers to the ID of the language
     * @param language refers to the name of the language
     * @return a {@link List} containing {@link PhotopoemDTO} projections of the {@link Photopoem} objects
     */
    public List<PhotopoemDTO> filterPhotopoems(
            String title,
            String subtitle,
            String altTitle,
            String series,
            Long volume,
            Long issue,
            String publicationDate,
            Long pubMediumId,
            String pubMedium,
            Long pubPlaceId,
            Long locationId,
            Long authorId,
            String author,
            Long photographerId,
            String photographer,
            Long depictedPersonId,
            String depictedPerson,
            Long participantId,
            String participant,
            Long contributorId,
            String otherContributor,
            Long themeId,
            String theme,
            Long imageMotifId,
            String imageMotif,
            Long copyrightStatusImageId,
            String copyrightStatusImage,
            Long copyrightStatusTextId,
            String copyrightStatusText,
            Long languageId,
            String language
    ) {
        Specification<Photopoem> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasTitle(title));
        }
        if (subtitle != null && !subtitle.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasSubtitle(subtitle));
        }
        if (altTitle != null && !altTitle.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasAltTitle(altTitle));
        }
        if (series != null && !series.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasSeries(series));
        }
        if (volume != null) {
            spec = spec.and(PhotopoemSpecification.hasVolume(volume));
        }
        if (issue != null) {
            spec = spec.and(PhotopoemSpecification.hasIssue(issue));
        }
        if (publicationDate != null && !publicationDate.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasPublicationDate(publicationDate));
        }
        if (pubMediumId != null) {
            spec = spec.and(PhotopoemSpecification.hasPubMediumId(pubMediumId));
        }
        if (pubMedium != null && !pubMedium.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasPubMedium(pubMedium));
        }
        if (pubPlaceId != null) {
            spec = spec.and(PhotopoemSpecification.hasPubPlaceId(pubPlaceId));
        }
        if (locationId != null){
            spec = spec.and(PhotopoemSpecification.hasLocationId(locationId));
        }
        if (authorId != null) {
            spec = spec.and(PhotopoemSpecification.hasAuthorId(authorId));
        }
        if (author != null && !author.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasAuthor(author));
        }
        if (photographerId != null) {
            spec = spec.and(PhotopoemSpecification.hasPhotographerId(photographerId));
        }
        if (photographer != null && !photographer.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasPhotographer(photographer));
        }
        if (depictedPersonId != null) {
            spec = spec.and(PhotopoemSpecification.hasDepictedPersonId(depictedPersonId));
        }
        if (depictedPerson != null && !depictedPerson.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasDepictedPerson(depictedPerson));
        }
        if (participantId != null) {
            spec = spec.and(PhotopoemSpecification.hasParticipantId(participantId));
        }
        if (participant != null && !participant.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasParticipant(participant));
        }
        if (contributorId != null) {
            spec = spec.and(PhotopoemSpecification.hasOtherContributorId(contributorId));
        }
        if (otherContributor != null && !otherContributor.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasOtherContributor(otherContributor));
        }
        if (themeId != null) {
            spec = spec.and(PhotopoemSpecification.hasThemeId(themeId));
        }
        if (theme != null && !theme.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasTheme(theme));
        }
        if (imageMotifId != null) {
            spec = spec.and(PhotopoemSpecification.hasImageMotifId(imageMotifId));
        }
        if (imageMotif != null && !imageMotif.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasImageMotif(imageMotif));
        }
        if (copyrightStatusImageId != null) {
            spec = spec.and(PhotopoemSpecification.hasCopyrightStatusImageId(copyrightStatusImageId));
        }
        if (copyrightStatusImage != null && !copyrightStatusImage.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasCopyrightStatusImage(copyrightStatusImage));
        }
        if (copyrightStatusTextId != null) {
            spec = spec.and(PhotopoemSpecification.hasCopyrightStatusTextId(copyrightStatusTextId));
        }
        if (copyrightStatusText != null && !copyrightStatusText.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasCopyrightStatusText(copyrightStatusText));
        }
        if (languageId != null) {
            spec = spec.and(PhotopoemSpecification.hasLanguageId(languageId));
        }
        if (language != null && !language.isEmpty()) {
            spec = spec.and(PhotopoemSpecification.hasLanguage(language));
        }

        List<Photopoem> result = photopoemRepository.findAll(spec);
        return result.stream()
                .map(photopoemMapper::PhotopoemToPhotopoemDTO)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PhotopoemDTO::getId)).toList();
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
