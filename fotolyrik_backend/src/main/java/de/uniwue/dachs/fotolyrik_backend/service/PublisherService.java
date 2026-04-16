package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import de.uniwue.dachs.fotolyrik_backend.repository.PublisherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * @return a {@link List} of available {@link Publisher} objects
     */
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    /**
     * @param id ID of the {@link Publisher} to be found
     * @return existing {@link Publisher} entry
     */
    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    /**
     * @param publisher {@link Publisher} object to be created
     * @return created {@link Publisher} object
     */
    @Transactional
    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    /**
     * @param id ID of the {@link Publisher} object to be updated
     * @param publisher {@link Publisher} object
     * @return updated {@link Publisher} object
     */
    @Transactional
    public Publisher updatePublisher(Long id, Publisher publisher) {
        return publisherRepository.findById(id)
                .map(entity -> {
                    entity.mapBaseEntityFields(publisher);
                    entity.setName(publisher.getName());
                    entity.setDescription(publisher.getDescription());
                    return publisherRepository.save(entity);
                }).orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id ID of the {@link Publisher} object to be deleted
     */
    @Transactional
    public void deletePublisherById(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        }
        publisherRepository.deleteById(id);
    }
}
