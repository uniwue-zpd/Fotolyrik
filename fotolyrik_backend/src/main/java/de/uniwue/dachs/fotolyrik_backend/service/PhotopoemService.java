package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemDTO;
import de.uniwue.dachs.fotolyrik_backend.model.*;
import de.uniwue.dachs.fotolyrik_backend.repository.*;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.*;
import jakarta.persistence.EntityNotFoundException;
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

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            FullTextService fullTextService,
                            PhotopoemMapper photopoemMapper,
                            PubMediumMapper pubMediumMapper,
                            PersonMapper personMapper,
                            KeywordMapper keywordMapper,
                            FileMapper fileMapper) {
        this.photopoemRepository = photopoemRepository;
        this.fullTextService = fullTextService;
        this.photopoemMapper = photopoemMapper;
        this.pubMediumMapper = pubMediumMapper;
        this.personMapper = personMapper;
        this.keywordMapper = keywordMapper;
        this.fileMapper = fileMapper;
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
            entity.setPageCount(updatedPhotopoem.getPageCount());
            entity.setPublicationDate(updatedPhotopoem.getPublicationDate());
            entity.setPublicationMedium(pubMediumMapper.PubMediumDTOToPubmedium(updatedPhotopoem.getPublicationMedium()));
            entity.setAuthors(personMapper.PersonDTOsToPersons(updatedPhotopoem.getAuthors()));
            entity.setPhotographers(personMapper.PersonDTOsToPersons(updatedPhotopoem.getPhotographers()));
            entity.setOtherContributors(personMapper.PersonDTOsToPersons(updatedPhotopoem.getOtherContributors()));
            entity.setThemes(keywordMapper.KeywordDTOsToKeywords(updatedPhotopoem.getThemes()));
            entity.setImageMotifs(keywordMapper.KeywordDTOsToKeywords(updatedPhotopoem.getImageMotifs()));
            entity.setForm(updatedPhotopoem.getForm());
            entity.setLink(updatedPhotopoem.getLink());
            entity.setIiifManifest(updatedPhotopoem.getIiifManifest());
            entity.setImages(fileMapper.FileDTOsToFiles(updatedPhotopoem.getImages()));
            entity.setCopyrightStatusImage(updatedPhotopoem.getCopyrightStatusImage());
            entity.setCopyrightStatusText(updatedPhotopoem.getCopyrightStatusText());
            entity.setLanguages(updatedPhotopoem.getLanguages());

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
