package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PhotopoemService {
    private final PhotopoemRepository photopoemRepository;
    private final PersonRepository personRepository;
    private final PubMediumRepository pubMediumRepository;

    public PhotopoemService(PhotopoemRepository photopoemRepository,
                            PersonRepository personRepository,
                            PubMediumRepository pubMediumRepository) {
        this.photopoemRepository = photopoemRepository;
        this.personRepository = personRepository;
        this.pubMediumRepository = pubMediumRepository;
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
        photopoem.setPublicationMedium(getOrSavePubMedium(photopoem.getPublicationMedium()));
        return photopoemRepository.save(photopoem);
    }

    @Transactional
    public Photopoem updatePhotopoem(Long id, Photopoem updatedPhotopoem) {
        return photopoemRepository.findById(id).map(field -> {
            if (updatedPhotopoem.getTitle() != null) {
                field.setTitle(updatedPhotopoem.getTitle());
            }
            if (updatedPhotopoem.getVolume() != null) {
                field.setVolume(updatedPhotopoem.getVolume());
            }
            if (updatedPhotopoem.getIssue() != null) {
                field.setIssue(updatedPhotopoem.getIssue());
            }
            if (updatedPhotopoem.getPageNumber() != null) {
                field.setPageNumber(updatedPhotopoem.getPageNumber());
            }
            if (updatedPhotopoem.getPublicationDate() != null) {
                field.setPublicationDate(updatedPhotopoem.getPublicationDate());
            }
            if (updatedPhotopoem.getPublicationMedium() != null) {
                field.setPublicationMedium(getOrSavePubMedium(updatedPhotopoem.getPublicationMedium()));
            }
            if (updatedPhotopoem.getAuthor() != null) {
                field.setAuthor(getOrSavePerson(updatedPhotopoem.getAuthor()));
            }
            if (updatedPhotopoem.getPhotographer() != null) {
                field.setPhotographer(getOrSavePerson(updatedPhotopoem.getPhotographer()));
            }
            if (updatedPhotopoem.getLink() != null) {
                field.setLink(updatedPhotopoem.getLink());
            }
            if (updatedPhotopoem.getIiifManifest() != null) {
                field.setIiifManifest(updatedPhotopoem.getIiifManifest());
            }
            return photopoemRepository.save(field);
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
}
