package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import de.uniwue.dachs.fotolyrik_backend.repository.PublisherRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
            publisher.setDescription(publisherDTO.getDescription());
            publisher.setBaseEntityFields(publisherDTO);
            publisherRepository.save(publisher);
            return publisher;
        }
    }

    public PublisherDTO PublisherToPublisherDTO(Publisher publisher) {
        if (publisher == null) return null;
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        publisherDTO.setDescription(publisher.getDescription());
        publisherDTO.setBaseEntityFields(publisher);
        return publisherDTO;
    }

    public Set<Publisher> PublisherDTOsToPublishers(Set<PublisherDTO> publisherDTOs) {
        return MapperUtils.mapSet(publisherDTOs, this::PublisherDTOToPublisher);
    }
    public Set<PublisherDTO> PublishersToPublisherDTOs(Set<Publisher> publishers) {
        return MapperUtils.mapSet(publishers, this::PublisherToPublisherDTO);
    }
    public List<Publisher> PublisherDTOsToPublishers(List<PublisherDTO> publisherDTOs) {
        return MapperUtils.mapList(publisherDTOs, this::PublisherDTOToPublisher);
    }
    public List<PublisherDTO> PublishersToPublisherDTOs(List<Publisher> publishers) {
        return MapperUtils.mapList(publishers, this::PublisherToPublisherDTO);
    }

    public Publisher PublisherPreviewDTOToPublisher(PublisherPreviewDTO publisherPreviewDTO) {
        if (publisherPreviewDTO == null || publisherPreviewDTO.getId() == null) return null;
        return publisherRepository.findById(publisherPreviewDTO.getId()).orElse(null);
    }

    public PublisherPreviewDTO PublisherToPublisherPreviewDTO(Publisher publisher) {
        if (publisher == null) return null;
        PublisherPreviewDTO publisherPreviewDTO = new PublisherPreviewDTO();
        publisherPreviewDTO.setId(publisher.getId());
        publisherPreviewDTO.setName(publisher.getName());
        return  publisherPreviewDTO;
    }

    public Set<Publisher> PublisherPreviewDTOsToPublishers(Set<PublisherPreviewDTO> publisherPreviewDTOs) {
        return MapperUtils.mapSet(publisherPreviewDTOs, this::PublisherPreviewDTOToPublisher);
    }
    public Set<PublisherPreviewDTO> PublishersToPublisherPreviewDTOs(Set<Publisher> publishers) {
        return MapperUtils.mapSet(publishers, this::PublisherToPublisherPreviewDTO);
    }
    public List<Publisher> PublisherPreviewDTOsToPublishers(List<PublisherPreviewDTO> publisherPreviewDTOs) {
        return MapperUtils.mapList(publisherPreviewDTOs, this::PublisherPreviewDTOToPublisher);
    }
    public List<PublisherPreviewDTO> PublishersToPublisherPreviewDTOs(List<Publisher> publishers) {
        return MapperUtils.mapList(publishers, this::PublisherToPublisherPreviewDTO);
    }

}
