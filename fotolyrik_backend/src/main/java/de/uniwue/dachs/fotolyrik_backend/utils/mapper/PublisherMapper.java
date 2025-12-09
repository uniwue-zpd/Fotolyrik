package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import de.uniwue.dachs.fotolyrik_backend.repository.PublisherRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PublisherMapper {
    private final PublisherRepository publisherRepository;

    public PublisherMapper(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher PublisherDTOToPublisher(PublisherDTO publisherDTO) {
        if (publisherDTO == null) return null;
        if (publisherDTO.getId() != null) {
            return publisherRepository.findById(publisherDTO.getId()).orElse(null);
        } else {
            Publisher publisher = new Publisher();
            publisher.setName(publisherDTO.getName());
            publisherRepository.save(publisher);
            return publisher;
        }
    }

    public Set<Publisher> PublisherDTOsToPublishers(Set<PublisherDTO> publisherDTOs) {
        if (publisherDTOs.isEmpty()) return Collections.emptySet();
        return publisherDTOs.stream()
                .map(this::PublisherDTOToPublisher)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public PublisherDTO PublisherToPublisherDTO(Publisher publisher) {
        if (publisher == null) return null;
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        return publisherDTO;
    }

    public Set<PublisherDTO> PublishersToPublisherDTOs(Set<Publisher> publishers) {
        if (publishers.isEmpty()) return Collections.emptySet();
        return publishers.stream()
                .map(this::PublisherToPublisherDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
