package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
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

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            PersonRepository personRepository,
                            PubMediumRepository pubMediumRepository,
                            FileRepository fileRepository) {
        this.photopoemRepository = photopoemRepository;
        this.personRepository = personRepository;
        this.pubMediumRepository = pubMediumRepository;
        this.fileRepository = fileRepository;
    }

    public List<Photopoem> getAllPhotopoems() {
        return photopoemRepository.findAll();
    }

    public Optional<Photopoem> getPhotopoemById(Long id) {
        return photopoemRepository.findById(id);
    }

    @Transactional
    public Photopoem savePhotopoem(Photopoem photopoem) {
        photopoem.setAuthor(getOrSavePerson(photopoem.getAuthor()));
        photopoem.setPhotographer(getOrSavePerson(photopoem.getPhotographer()));
        photopoem.setPublication_medium(getOrSavePubMedium(photopoem.getPublication_medium()));
        return photopoemRepository.save(photopoem);
    }

    @Transactional
    public Photopoem updatePhotopoem(Long id, Photopoem updatedPhotopoem) {
        return photopoemRepository.findById(id).map(entity -> {
            entity.setTitle(updatedPhotopoem.getTitle());
            entity.setVolume((updatedPhotopoem.getVolume() != null) ? updatedPhotopoem.getVolume() : null);
            entity.setIssue((updatedPhotopoem.getIssue() != null) ? updatedPhotopoem.getIssue() : null);
            entity.setPage_number((updatedPhotopoem.getPage_number() != null) ? updatedPhotopoem.getPage_number() : null);
            entity.setPage_count((updatedPhotopoem.getPage_count() != null) ? updatedPhotopoem.getPage_count() : null);
            entity.setPublication_date((updatedPhotopoem.getPublication_date() != null) ? updatedPhotopoem.getPublication_date() : null);
            entity.setPublication_medium((updatedPhotopoem.getPublication_medium() != null) ? getOrSavePubMedium(updatedPhotopoem.getPublication_medium()) : null);
            entity.setAuthor((updatedPhotopoem.getAuthor() != null) ? getOrSavePerson(updatedPhotopoem.getAuthor()) : null);
            entity.setPhotographer((updatedPhotopoem.getPhotographer() != null) ? getOrSavePerson(updatedPhotopoem.getPhotographer()) : null);
            entity.setTopics((updatedPhotopoem.getTopics() != null) ? updatedPhotopoem.getTopics() : null);
            entity.setLink((updatedPhotopoem.getLink() != null) ? updatedPhotopoem.getLink() : null);
            entity.setIiif_manifest((updatedPhotopoem.getIiif_manifest() != null) ? updatedPhotopoem.getIiif_manifest() : null);
            entity.setImages((updatedPhotopoem.getImages() != null) ? getFiles(updatedPhotopoem.getImages()) : null);
            return photopoemRepository.save(entity);
        }).orElseThrow(() -> new RuntimeException("Photopoem with id '" + id + "' does not exist"));
    }

    @Transactional
    public void deletePhotopoem(Long id) {
        if (!photopoemRepository.existsById(id)) {
            throw new RuntimeException("Photopoem with id '" + id + "' does not exist");
        }
        photopoemRepository.deleteById(id);
    }

    // Helper method to set an existing person or save a new one
    private Person getOrSavePerson(Person person) {
        if (person == null) {
            return null;
        }
        if (person.getId() != null) {
            return personRepository.findById(person.getId()).orElse(null);
        }
        return personRepository.save(person);
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
        for (File file : files) {
            if (file.getId() != null) {
                fileRepository.findById(file.getId()).ifPresent(newFiles::add);
            }
        }
        return newFiles;
    }
}
