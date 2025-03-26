package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/photopoems")
public class PhotopoemController {
    @Autowired
    private PhotopoemRepository photopoemRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public Iterable<Photopoem> getPhotopoems() {
        return photopoemRepository.findAll();
    }

    @GetMapping("/{id}")
    public Photopoem getPhotopoemById(@PathVariable Long id) {
        return photopoemRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Photopoem> savePhotopoem(@RequestBody Photopoem photopoem) {
        if (photopoem.getAuthor() != null) {
            if (photopoem.getAuthor().getId() != null) {
                Person existingAuthor = personRepository.findById(photopoem.getAuthor().getId()).orElse(null);
                photopoem.setAuthor(existingAuthor);
            } else {
                Person newAuthor = personRepository.save(photopoem.getAuthor());
                photopoem.setAuthor(newAuthor);
            }
        }
        if (photopoem.getPhotographer() != null) {
            if (photopoem.getPhotographer().getId() != null) {
                Person existingPhotographer = personRepository.findById(photopoem.getPhotographer().getId()).orElse(null);
                photopoem.setPhotographer(existingPhotographer);
            } else {
                Person newPhotographer = personRepository.save(photopoem.getPhotographer());
                photopoem.setPhotographer(newPhotographer);
            }
        }
        Photopoem savedPhotopoem = photopoemRepository.save(photopoem);
        return ResponseEntity.ok(savedPhotopoem);
    }

    //TODO: Implement PUT and DELETE methods
}
