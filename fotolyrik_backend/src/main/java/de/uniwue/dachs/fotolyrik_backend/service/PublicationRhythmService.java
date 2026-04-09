package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import de.uniwue.dachs.fotolyrik_backend.repository.PublicationRhythmRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationRhythmService {
    private final PublicationRhythmRepository publicationRhythmRepository;

    public PublicationRhythmService(PublicationRhythmRepository publicationRhythmRepository) {
        this.publicationRhythmRepository = publicationRhythmRepository;
    }

    /**
     * @return a {@link List} of available {@link PublicationRhythm} objects
     */
    public List<PublicationRhythm> getAllPublicationRhythms() {
        return publicationRhythmRepository.findAll();
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be found
     * @return existing {@link PublicationRhythm} object
     */
    public Optional<PublicationRhythm> getPublicationRhythmById(Long id) {
        return publicationRhythmRepository.findById(id);
    }

    /**
     * @param publicationRhythm {@link PublicationRhythm} object to be created
     * @return created {@link PublicationRhythm} object
     */
    public PublicationRhythm createPublicationRhythm(PublicationRhythm publicationRhythm) {
        return publicationRhythmRepository.save(publicationRhythm);
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be updated
     * @param publicationRhythm {@link PublicationRhythm} object
     * @return updated {@link PublicationRhythm} object
     */
    public PublicationRhythm updatePublicationRhythm(Long id, PublicationRhythm publicationRhythm) {
        return publicationRhythmRepository.findById(id)
                .map(entity -> {
                    entity.mapBaseEntityFields(publicationRhythm);
                    entity.setValue(publicationRhythm.getValue());
                    entity.setDescription(publicationRhythm.getDescription());
                    return publicationRhythmRepository.save(entity);
                }).orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id ID of the {@link PublicationRhythm} object to be deleted
     */
    public void deletePublicationRhythmById(Long id) {
        if (!publicationRhythmRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        }
        publicationRhythmRepository.deleteById(id);
    }
}
