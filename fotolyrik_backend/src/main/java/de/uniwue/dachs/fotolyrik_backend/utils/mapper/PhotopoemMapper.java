package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import org.springframework.stereotype.Component;

@Component
public class PhotopoemMapper {
    private final PersonMapper personMapper;
    private final PubMediumMapper pubMediumMapper;
    private final KeywordMapper keywordMapper;
    private final FileMapper fileMapper;
    private final PhotopoemRepository photopoemRepository;
    private final LanguageMapper languageMapper;
    private final CopyrightStatusMapper copyrightStatusMapper;
    private final PublicationDateMapper publicationDateMapper;
    private final ContributionMapper contributionMapper;

    public PhotopoemMapper(PersonMapper personMapper, PubMediumMapper pubMediumMapper, KeywordMapper keywordMapper, FileMapper fileMapper, PhotopoemRepository photopoemRepository, LanguageMapper languageMapper, CopyrightStatusMapper copyrightStatusMapper, PublicationDateMapper publicationDateMapper, ContributionMapper contributionMapper) {
        this.personMapper = personMapper;
        this.pubMediumMapper = pubMediumMapper;
        this.keywordMapper = keywordMapper;
        this.fileMapper = fileMapper;
        this.photopoemRepository = photopoemRepository;
        this.languageMapper = languageMapper;
        this.copyrightStatusMapper = copyrightStatusMapper;
        this.publicationDateMapper = publicationDateMapper;
        this.contributionMapper = contributionMapper;
    }

    public Photopoem PhotopoemDTOToPhotopoem(PhotopoemDTO photopoemDTO) {
        Photopoem photopoem = new Photopoem();
        photopoem.setTitle(photopoemDTO.getTitle());
        photopoem.setSubtitle(photopoemDTO.getSubtitle());
        photopoem.setAltTitle(photopoemDTO.getAltTitle());
        photopoem.setVolume(photopoemDTO.getVolume());
        photopoem.setIssue(photopoemDTO.getIssue());
        photopoem.setPageNumber(photopoemDTO.getPageNumber());
        photopoem.setManifestPageNumber(photopoemDTO.getManifestPageNumber());
        photopoem.setPageCount(photopoemDTO.getPageCount());
        photopoem.setPictureCount(photopoemDTO.getPictureCount());
        photopoem.setPublicationDate(photopoemDTO.getPublicationDate());
        photopoem.setPublicationMedium(pubMediumMapper.PubMediumPreviewDTOToPubMedium(photopoemDTO.getPublicationMedium()));
        photopoem.setAuthors(personMapper.PreviewDTOsToPersons(photopoemDTO.getAuthors()));
        photopoem.setPhotographers(personMapper.PreviewDTOsToPersons(photopoemDTO.getPhotographers()));
        photopoem.setOtherContributors(personMapper.PreviewDTOsToPersons(photopoemDTO.getOtherContributors()));
        photopoem.setThemes(keywordMapper.KeywordDTOsToKeywords(photopoemDTO.getThemes()));
        photopoem.setImageMotifs(keywordMapper.KeywordDTOsToKeywords(photopoemDTO.getImageMotifs()));
        photopoem.setForm(photopoemDTO.getForm());
        photopoem.setLink(photopoemDTO.getLink());
        photopoem.setIiifManifest(photopoemDTO.getIiifManifest());
        photopoem.setImages(fileMapper.FileDTOsToFiles(photopoemDTO.getImages()));
        photopoem.setCopyrightStatusText(copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(photopoemDTO.getCopyrightStatusText()));
        photopoem.setCopyrightStatusImage(copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(photopoemDTO.getCopyrightStatusImage()));
        photopoem.setLanguages(languageMapper.LanguageDTOsToLanguages(photopoemDTO.getLanguages()));
        return photopoem;
    }

    public PhotopoemDTO PhotopoemToPhotopoemDTO(Photopoem photopoem) {
        PhotopoemDTO photopoemDTO = new PhotopoemDTO();
        photopoemDTO.setId(photopoem.getId());
        photopoemDTO.setTitle(photopoem.getTitle());
        photopoemDTO.setSubtitle(photopoem.getSubtitle());
        photopoemDTO.setAltTitle(photopoem.getAltTitle());
        photopoemDTO.setVolume(photopoem.getVolume());
        photopoemDTO.setIssue(photopoem.getIssue());
        photopoemDTO.setPageNumber(photopoem.getPageNumber());
        photopoemDTO.setManifestPageNumber(photopoem.getManifestPageNumber());
        photopoemDTO.setPageCount(photopoem.getPageCount());
        photopoemDTO.setPictureCount(photopoem.getPictureCount());
        photopoemDTO.setPublicationDate(publicationDateMapper.DateToDateWithoutDashes(photopoem.getPublicationDate()));
        photopoemDTO.setPublicationMedium(pubMediumMapper.PubMediumToPubMediumPreviewDTO(photopoem.getPublicationMedium()));
        photopoemDTO.setAuthors(personMapper.PersonsToPersonDTOs(photopoem.getAuthors()));
        photopoemDTO.setPhotographers(personMapper.PersonsToPersonDTOs(photopoem.getPhotographers()));
        photopoemDTO.setOtherContributors(personMapper.PersonsToPersonDTOs(photopoem.getOtherContributors()));
        photopoemDTO.setContributions(contributionMapper.ContributionsToContributionDTOs(photopoem.getContributions()));
        photopoemDTO.setThemes(keywordMapper.KeywordToKeywordDTOs(photopoem.getThemes()));
        photopoemDTO.setImageMotifs(keywordMapper.KeywordToKeywordDTOs(photopoem.getImageMotifs()));
        photopoemDTO.setForm(photopoem.getForm());
        photopoemDTO.setLink(photopoem.getLink());
        photopoemDTO.setIiifManifest(photopoem.getIiifManifest());
        photopoemDTO.setImages(fileMapper.FilesToFileDTOs(photopoem.getImages()));
        photopoemDTO.setCopyrightStatusImage(copyrightStatusMapper.CopyrightStatusToCopyrightStatusDTO(photopoem.getCopyrightStatusImage()));
        photopoemDTO.setCopyrightStatusText(copyrightStatusMapper.CopyrightStatusToCopyrightStatusDTO(photopoem.getCopyrightStatusText()));
        photopoemDTO.setLanguages(languageMapper.LanguagesToLanguageDTOs(photopoem.getLanguages()));
        photopoemDTO.setCreatedDate(photopoem.getCreatedDate());
        photopoemDTO.setCreatedBy(photopoem.getCreatedBy());
        photopoemDTO.setLastModifiedDate(photopoem.getLastModifiedDate());
        photopoemDTO.setLastModifiedBy(photopoem.getLastModifiedBy());
        return photopoemDTO;
    }

    public Photopoem PhotopoemPreviewDTOToPhotopoem(PhotopoemPreviewDTO photopoemPreviewDTO) {
        if (photopoemPreviewDTO == null || photopoemPreviewDTO.getId() == null) return null;
        return photopoemRepository.findById(photopoemPreviewDTO.getId()).orElse(null);
    }

    public PhotopoemPreviewDTO PhotopoemToPhotopoemPreviewDTO(Photopoem photopoem) {
        PhotopoemPreviewDTO photopoemPreviewDTO = new PhotopoemPreviewDTO();
        photopoemPreviewDTO.setId(photopoem.getId());
        photopoemPreviewDTO.setTitle(photopoem.getTitle());
        photopoemPreviewDTO.setAltTitle(photopoem.getAltTitle());
        return photopoemPreviewDTO;
    }
}
