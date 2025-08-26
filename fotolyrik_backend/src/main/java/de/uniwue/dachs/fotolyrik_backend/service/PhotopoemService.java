package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PhotopoemService {
    private final PhotopoemRepository photopoemRepository;
    private final PersonRepository personRepository;
    private final PubMediumRepository pubMediumRepository;
    private final FileRepository fileRepository;
    private final FullTextService fullTextService;

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            PersonRepository personRepository,
                            PubMediumRepository pubMediumRepository,
                            FileRepository fileRepository, FullTextService fullTextService) {
        this.photopoemRepository = photopoemRepository;
        this.personRepository = personRepository;
        this.pubMediumRepository = pubMediumRepository;
        this.fileRepository = fileRepository;
        this.fullTextService = fullTextService;
    }

    public List<Photopoem> getAllPhotopoems() {
        return photopoemRepository.findAll();
    }

    public Optional<Photopoem> getPhotopoemById(Long id) {
        return photopoemRepository.findById(id);
    }

    public List<Photopoem> getPhotopoemsByAuthorId(Long author_id) {
        return photopoemRepository.findAllByAuthors_Id(author_id);
    }

    public List<Photopoem> getPhotopoemsByPhotographerId(Long photographer_id) {
        return photopoemRepository.findAllByPhotographers_Id(photographer_id);
    }

    public List<Photopoem> getPhotopoemsByAuthorIdAndPhotographerId(Long author_id, Long photographer_id) {
        return photopoemRepository.findAllByAuthors_IdAndPhotographers_id(author_id, photographer_id);
    }

    @Transactional
    public Photopoem savePhotopoem(Photopoem photopoem) {
        photopoem.setAuthors(getOrSavePersons(photopoem.getAuthors()));
        photopoem.setPhotographers(getOrSavePersons(photopoem.getPhotographers()));
        photopoem.setOtherContributors(getOrSavePersons(photopoem.getOtherContributors()));
        photopoem.setPublicationMedium(getOrSavePubMedium(photopoem.getPublicationMedium()));
        return photopoemRepository.save(photopoem);
    }

    @Transactional
    public Photopoem updatePhotopoem(Long id, Photopoem updatedPhotopoem) {
        return photopoemRepository.findById(id).map(entity -> {
            entity.setTitle(updatedPhotopoem.getTitle());
            entity.setSubtitle(updatedPhotopoem.getSubtitle());
            entity.setAltTitle(updatedPhotopoem.getAltTitle());
            entity.setVolume((updatedPhotopoem.getVolume() != null) ? updatedPhotopoem.getVolume() : null);
            entity.setIssue((updatedPhotopoem.getIssue() != null) ? updatedPhotopoem.getIssue() : null);
            entity.setPageNumber((updatedPhotopoem.getPageNumber() != null) ? updatedPhotopoem.getPageNumber() : null);
            entity.setPageCount((updatedPhotopoem.getPageCount() != null) ? updatedPhotopoem.getPageCount() : null);
            entity.setPublicationDate((updatedPhotopoem.getPublicationDate() != null) ? updatedPhotopoem.getPublicationDate() : null);
            entity.setPublicationMedium((updatedPhotopoem.getPublicationMedium() != null) ? getOrSavePubMedium(updatedPhotopoem.getPublicationMedium()) : null);
            entity.setAuthors((updatedPhotopoem.getAuthors() != null) ? getOrSavePersons(updatedPhotopoem.getAuthors()) : null);
            entity.setPhotographers((updatedPhotopoem.getPhotographers() != null) ? getOrSavePersons(updatedPhotopoem.getPhotographers()) : null);
            entity.setOtherContributors((updatedPhotopoem.getOtherContributors() != null) ? getOrSavePersons(updatedPhotopoem.getOtherContributors()) : new HashSet<>());
            entity.setThemes((updatedPhotopoem.getThemes() != null) ? updatedPhotopoem.getThemes() : null);
            entity.setTopics((updatedPhotopoem.getTopics() != null) ? updatedPhotopoem.getTopics() : null);
            entity.setForm((updatedPhotopoem.getForm() != null) ? updatedPhotopoem.getForm() : null);
            entity.setLink((updatedPhotopoem.getLink() != null) ? updatedPhotopoem.getLink() : null);
            entity.setIiifManifest((updatedPhotopoem.getIiifManifest() != null) ? updatedPhotopoem.getIiifManifest() : null);
            entity.setImages((updatedPhotopoem.getImages() != null) ? getFiles(updatedPhotopoem.getImages()) : null);
            entity.setCopyrightStatusImage((updatedPhotopoem.getCopyrightStatusImage() != null) ? updatedPhotopoem.getCopyrightStatusImage() : null);
            entity.setCopyrightStatusText((updatedPhotopoem.getCopyrightStatusText() != null) ? updatedPhotopoem.getCopyrightStatusText() : null);
            entity.setLanguages((updatedPhotopoem.getLanguages() != null) ? updatedPhotopoem.getLanguages() : null);
            return photopoemRepository.save(entity);
        }).orElseThrow(() -> new EntityNotFoundException("Photopoem with id '" + id + "' does not exist"));
    }

    @Transactional
    public void deletePhotopoem(Long id) {
        if (!photopoemRepository.existsById(id)) {
            throw new EntityNotFoundException("Photopoem with id '" + id + "' does not exist");
        }
        fullTextService.deleteFullTextByPhotopoemID(id);
        photopoemRepository.deleteById(id);
    }

    // Helper method to set existing persons or save new ones
    private Set<Person> getOrSavePersons(Set<Person> persons) {
        if (persons == null || persons.isEmpty()) {
            return new HashSet<>();
        }
        Set<Person> savedPersons = new HashSet<>();
        persons.forEach(person -> {
            if (person.getId() != null) {
                savedPersons.add(personRepository.findById(person.getId()).orElse(null));
            } else {
                savedPersons.add(personRepository.save(person));
            }
        });
        return savedPersons;
    }

    // Helper method to set an existing publication medium or save a new one
    private PubMedium getOrSavePubMedium(PubMedium pubMedium) {
        if (pubMedium == null) {
            return null;
        }
        if (pubMedium.getId() != null) {
            return pubMediumRepository.findById(pubMedium.getId()).orElse(null);
        }
        return pubMediumRepository.save(pubMedium);
    }

    // Helper method to assign existing files to the photopoem
    private Set<File> getFiles(Set<File> files) {
        if (files == null) {
            return null;
        }
        Set<File> newFiles = new HashSet<>();
        files.forEach(file -> {
            if (file.getId() != null) {
                fileRepository.findById(file.getId()).ifPresent(newFiles::add);
            }
        });
        return newFiles;
    }
}
