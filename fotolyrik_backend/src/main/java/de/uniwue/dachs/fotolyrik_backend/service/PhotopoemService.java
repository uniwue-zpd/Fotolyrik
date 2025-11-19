package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.model.*;
import de.uniwue.dachs.fotolyrik_backend.repository.*;
import de.uniwue.dachs.fotolyrik_backend.specification.PhotopoemSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PhotopoemService {
    private final PhotopoemRepository photopoemRepository;
    private final FullTextService fullTextService;
    private final PhotopoemMapper photopoemMapper;
    private final PubMediumMapper pubMediumMapper;
    private final PersonMapper personMapper;
    private final KeywordMapper keywordMapper;
    private final FileMapper fileMapper;
    private final CopyrightStatusMapper copyrightStatusMapper;
    private final LanguageMapper languageMapper;

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            FullTextService fullTextService,
                            PhotopoemMapper photopoemMapper,
                            PubMediumMapper pubMediumMapper,
                            PersonMapper personMapper,
                            KeywordMapper keywordMapper,
                            FileMapper fileMapper, CopyrightStatusMapper copyrightStatusMapper, LanguageMapper languageMapper) {
        this.photopoemRepository = photopoemRepository;
        this.fullTextService = fullTextService;
        this.photopoemMapper = photopoemMapper;
        this.pubMediumMapper = pubMediumMapper;
        this.personMapper = personMapper;
        this.keywordMapper = keywordMapper;
        this.fileMapper = fileMapper;
        this.copyrightStatusMapper = copyrightStatusMapper;
        this.languageMapper = languageMapper;
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
     * Filter photopoems based on various criteria.
     * @param title refers to the title of the photopoem
     * @param subtitle refers to the subtitle of the photopoem
     * @param altTitle refers to the alternative title of the photopoem {@link String}
     * @param volume refers to the volume number {@link Long}
     * @param issue refers to the issue number {@link Long}
     * @param publicationDate refers to the publication date {@link String}
     * @param pubMediumId refers to the ID of the publication medium {@link Long}
     * @param pubMedium refers to the name of the publication medium {@link String}
     * @param authorId refers to the ID of the author {@link Long}
     * @param author refers to the name of the author {@link String}
     * @param photographerId refers to the ID of the photographer {@link Long}
     * @param photographer refers to the name of the photographer {@link String}
     * @param contributorId refers to the ID of other contributors {@link Long}
     * @param otherContributor refers to the name of other contributors {@link String}
     * @param themeId refers to the ID of the theme {@link Long}
     * @param theme refers to the name of the theme {@link String}
     * @param imageMotifId refers to the ID of the image motif {@link Long}
     * @param imageMotif refers to the name of the image motif {@link String}
     * @param copyrightStatusImageId refers to the ID of the copyright status for the image {@link Long}
     * @param copyrightStatusImage refers to the name of the copyright status for the image {@link String}
     * @param copyrightStatusTextId refers to the ID of the copyright status for the text {@link Long}
     * @param copyrightStatusText refers to the name of the copyright status for the text {@link String}
     * @param languageId refers to the ID of the language {@link Long}
     * @param language refers to the name of the language {@link String}
     * @return a {@link List} containing {@link PhotopoemDTO} projections of the {@link Photopoem} objects
     */
    public List<PhotopoemDTO> filterPhotopoems(
            String title,
            String subtitle,
            String altTitle,
            Long volume,
            Long issue,
            String publicationDate,
            Long pubMediumId,
            String pubMedium,
            Long authorId,
            String author,
            Long photographerId,
            String photographer,
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
     *
     * @param author_id of the {@link Person}
     * @return a {@link List} containing the mapped {@link PhotopoemDTO}
     */
    public List<PhotopoemDTO> getPhotopoemsByAuthorId(Long author_id) {
        return photopoemRepository.findAllByAuthors_Id(author_id)
                .stream()
                .map(photopoemMapper::PhotopoemToPhotopoemDTO)
                .sorted(Comparator.comparing(PhotopoemDTO::getId)).toList();
    }

    /**
     * @param photographer_id of the {@link Person}
     * @return a {@link List} containing the mapped {@link PhotopoemDTO}
     */
    public List<PhotopoemDTO> getPhotopoemsByPhotographerId(Long photographer_id) {
        return photopoemRepository.findAllByPhotographers_Id(photographer_id)
                .stream()
                .map(photopoemMapper::PhotopoemToPhotopoemDTO)
                .sorted(Comparator.comparing(PhotopoemDTO::getId)).toList();
    }

    /**
     * @param author_id ID of the author
     * @param photographer_id ID of the photographer
     * @return a {@link List} containing the mapped {@link PhotopoemDTO}
     */
    public List<PhotopoemDTO> getPhotopoemsByAuthorIdAndPhotographerId(Long author_id, Long photographer_id) {
        return photopoemRepository.findAllByAuthors_IdAndPhotographers_id(author_id, photographer_id)
                .stream()
                .map(photopoemMapper::PhotopoemToPhotopoemDTO)
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
        return photopoemMapper.PhotopoemToPhotopoemDTO(createdPhotopoem);
    }

    /**
     * @param id of the {@link Photopoem} object
     * @param updatedPhotopoem is an {@link PhotopoemDTO} object
     * @return {@link Photopoem} object and persists the updates
     */
    @Transactional
    public PhotopoemDTO updatePhotopoem(Long id, PhotopoemDTO updatedPhotopoem) {
        return photopoemRepository.findById(id).map(entity -> {
            entity.setTitle(updatedPhotopoem.getTitle());
            entity.setSubtitle(updatedPhotopoem.getSubtitle());
            entity.setAltTitle(updatedPhotopoem.getAltTitle());
            entity.setVolume(updatedPhotopoem.getVolume());
            entity.setIssue(updatedPhotopoem.getIssue());
            entity.setPageNumber(updatedPhotopoem.getPageNumber());
            entity.setManifestPageNumber(updatedPhotopoem.getManifestPageNumber());
            entity.setPageCount(updatedPhotopoem.getPageCount());
            entity.setPublicationDate(updatedPhotopoem.getPublicationDate());
            entity.setPublicationMedium(pubMediumMapper.PubMediumPreviewDTOToPubMedium(updatedPhotopoem.getPublicationMedium()));
            entity.setAuthors(personMapper.PersonDTOsToPersons(updatedPhotopoem.getAuthors()));
            entity.setPhotographers(personMapper.PersonDTOsToPersons(updatedPhotopoem.getPhotographers()));
            entity.setOtherContributors(personMapper.PersonDTOsToPersons(updatedPhotopoem.getOtherContributors()));
            entity.setThemes(keywordMapper.KeywordDTOsToKeywords(updatedPhotopoem.getThemes()));
            entity.setImageMotifs(keywordMapper.KeywordDTOsToKeywords(updatedPhotopoem.getImageMotifs()));
            entity.setForm(updatedPhotopoem.getForm());
            entity.setLink(updatedPhotopoem.getLink());
            entity.setIiifManifest(updatedPhotopoem.getIiifManifest());
            entity.setImages(fileMapper.FileDTOsToFiles(updatedPhotopoem.getImages()));
            entity.setCopyrightStatusImage(copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(updatedPhotopoem.getCopyrightStatusImage()));
            entity.setCopyrightStatusText(copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(updatedPhotopoem.getCopyrightStatusText()));
            entity.setLanguages(languageMapper.LanguageDTOsToLanguages(updatedPhotopoem.getLanguages()));

            Photopoem savedPhotopoem = photopoemRepository.save(entity);
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
}
