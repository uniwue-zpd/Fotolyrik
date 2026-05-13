package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PhotopoemPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import org.springframework.stereotype.Component;

@Component
public class PhotopoemMapper {
    private final PersonMapper personMapper;
    private final PubMediumMapper pubMediumMapper;
    private final LocationMapper locationMapper;
    private final KeywordMapper keywordMapper;
    private final FileMapper fileMapper;
    private final PhotopoemRepository photopoemRepository;
    private final LanguageMapper languageMapper;
    private final CopyrightStatusMapper copyrightStatusMapper;
    private final PublicationDateMapper publicationDateMapper;
    private final ContributionMapper contributionMapper;

    public PhotopoemMapper(PersonMapper personMapper, PubMediumMapper pubMediumMapper, KeywordMapper keywordMapper, FileMapper fileMapper, PhotopoemRepository photopoemRepository, LanguageMapper languageMapper, CopyrightStatusMapper copyrightStatusMapper, PublicationDateMapper publicationDateMapper, ContributionMapper contributionMapper, LocationMapper locationMapper) {
        this.personMapper = personMapper;
        this.pubMediumMapper = pubMediumMapper;
        this.locationMapper = locationMapper;
        this.keywordMapper = keywordMapper;
        this.fileMapper = fileMapper;
        this.photopoemRepository = photopoemRepository;
        this.languageMapper = languageMapper;
        this.copyrightStatusMapper = copyrightStatusMapper;
        this.publicationDateMapper = publicationDateMapper;
        this.contributionMapper = contributionMapper;
    }

    public void updatePhotopoemFromDTO(Photopoem photopoemToUpdate, PhotopoemDTO photopoemDTO) {
        photopoemToUpdate.setTitle(photopoemDTO.getTitle());
        photopoemToUpdate.setSubtitle(photopoemDTO.getSubtitle());
        photopoemToUpdate.setAltTitle(photopoemDTO.getAltTitle());
        photopoemToUpdate.setVolume(photopoemDTO.getVolume());
        photopoemToUpdate.setIssue(photopoemDTO.getIssue());
        photopoemToUpdate.setPageNumber(photopoemDTO.getPageNumber());
        photopoemToUpdate.setManifestPageNumber(photopoemDTO.getManifestPageNumber());
        photopoemToUpdate.setPageCount(photopoemDTO.getPageCount());
        photopoemToUpdate.setPictureCount(photopoemDTO.getPictureCount());
        photopoemToUpdate.setPublicationDate(photopoemDTO.getPublicationDate());
        photopoemToUpdate.setPublicationMedium(pubMediumMapper.PubMediumPreviewDTOToPubMedium(photopoemDTO.getPublicationMedium()));
        photopoemToUpdate.setFoundIn(locationMapper.LocationPreviewDTOsToLocations(photopoemDTO.getFoundIn()));
        photopoemToUpdate.setAuthors(personMapper.PreviewDTOsToPersons(photopoemDTO.getAuthors()));
        photopoemToUpdate.setPhotographers(personMapper.PreviewDTOsToPersons(photopoemDTO.getPhotographers()));
        photopoemToUpdate.setDepictedPeople(personMapper.PreviewDTOsToPersons(photopoemDTO.getDepictedPeople()));
        photopoemToUpdate.setOtherContributors(personMapper.PreviewDTOsToPersons(photopoemDTO.getOtherContributors()));
        photopoemToUpdate.setThemes(keywordMapper.KeywordPreviewDTOsToKeywords(photopoemDTO.getThemes()));
        photopoemToUpdate.setImageMotifs(keywordMapper.KeywordPreviewDTOsToKeywords(photopoemDTO.getImageMotifs()));
        photopoemToUpdate.setForm(photopoemDTO.getForm());
        photopoemToUpdate.setLink(photopoemDTO.getLink());
        photopoemToUpdate.setIiifManifest(photopoemDTO.getIiifManifest());
        photopoemToUpdate.setImages(fileMapper.FileDTOsToFiles(photopoemDTO.getImages()));
        photopoemToUpdate.setImagesVisible(photopoemDTO.getImagesVisible());
        photopoemToUpdate.setCopyrightStatusText(
                copyrightStatusMapper.CopyrightStatusPreviewDTOToCopyrightStatus(photopoemDTO.getCopyrightStatusText()));
        photopoemToUpdate.setCopyrightStatusImage(
                copyrightStatusMapper.CopyrightStatusPreviewDTOToCopyrightStatus(photopoemDTO.getCopyrightStatusImage()));
        photopoemToUpdate.setLanguages(languageMapper.LanguagePreviewDTOsToLanguages(photopoemDTO.getLanguages()));
    }
    
    public Photopoem PhotopoemDTOToPhotopoem(PhotopoemDTO photopoemDTO) {
        Photopoem photopoem = new Photopoem();
        photopoem.setContributions(contributionMapper.DTOsToContributions(photopoemDTO.getContributions(), photopoem));
        updatePhotopoemFromDTO(photopoem, photopoemDTO);
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
        photopoemDTO.setFoundIn(locationMapper.LocationsToLocationPreviewDTOs(photopoem.getFoundIn()));
        photopoemDTO.setAuthors(personMapper.PersonsToPreviewDTOs(photopoem.getAuthors()));
        photopoemDTO.setPhotographers(personMapper.PersonsToPreviewDTOs(photopoem.getPhotographers()));
        photopoemDTO.setDepictedPeople(personMapper.PersonsToPreviewDTOs(photopoem.getDepictedPeople()));
        photopoemDTO.setOtherContributors(personMapper.PersonsToPreviewDTOs(photopoem.getOtherContributors()));
        photopoemDTO.setContributions(contributionMapper.ContributionsToDTOs(photopoem.getContributions()));
        photopoemDTO.setThemes(keywordMapper.KeywordsToKeywordPreviewDTOs(photopoem.getThemes()));
        photopoemDTO.setImageMotifs(keywordMapper.KeywordsToKeywordPreviewDTOs(photopoem.getImageMotifs()));
        photopoemDTO.setForm(photopoem.getForm());
        photopoemDTO.setLink(photopoem.getLink());
        photopoemDTO.setIiifManifest(photopoem.getIiifManifest());
        photopoemDTO.setImages(fileMapper.FilesToFileDTOs(photopoem.getImages()));
        photopoemDTO.setImagesVisible(photopoem.getImagesVisible());
        photopoemDTO.setCopyrightStatusImage(
                copyrightStatusMapper.CopyrightStatusToCopyrightStatusPreviewDTO(photopoem.getCopyrightStatusImage()));
        photopoemDTO.setCopyrightStatusText(
                copyrightStatusMapper.CopyrightStatusToCopyrightStatusPreviewDTO(photopoem.getCopyrightStatusText()));
        photopoemDTO.setLanguages(languageMapper.LanguagesToLanguagePreviewDTOs(photopoem.getLanguages()));
        photopoemDTO.setBaseEntityFields(photopoem);
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
