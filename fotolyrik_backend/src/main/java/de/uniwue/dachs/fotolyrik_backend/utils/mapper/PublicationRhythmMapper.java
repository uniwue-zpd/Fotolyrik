package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublicationRhythmDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import de.uniwue.dachs.fotolyrik_backend.repository.PublicationRhythmRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PublicationRhythmMapper {
    private final PublicationRhythmRepository publicationRhythmRepository;

    public PublicationRhythmMapper(PublicationRhythmRepository publicationRhythmRepository) {
        this.publicationRhythmRepository = publicationRhythmRepository;
    }

    public PublicationRhythm PublicationRhythmDTOToPublicationRhythm(PublicationRhythmDTO publicationRhythmDTO) {
        if (publicationRhythmDTO == null) return null;
        if (publicationRhythmDTO.getId() != null) {
            return publicationRhythmRepository.findById(publicationRhythmDTO.getId()).orElse(null);
        } else {
            PublicationRhythm publicationRhythm = new PublicationRhythm();
            publicationRhythm.setValue(publicationRhythmDTO.getValue());
            publicationRhythm.setDescription(publicationRhythmDTO.getDescription());
            publicationRhythmRepository.save(publicationRhythm);
            return publicationRhythm;
        }
    }

    public PublicationRhythmDTO PublicationRhythmToPublicationRhythmDTO(PublicationRhythm publicationRhythm) {
        if (publicationRhythm == null) return null;
        PublicationRhythmDTO publicationRhythmDTO = new PublicationRhythmDTO();
        publicationRhythmDTO.setId(publicationRhythm.getId());
        publicationRhythmDTO.setValue(publicationRhythm.getValue());
        publicationRhythmDTO.setDescription(publicationRhythm.getDescription());
        publicationRhythmDTO.setBaseEntityFields(publicationRhythm);
        return publicationRhythmDTO;
    }

    public Set<PublicationRhythmDTO> PublicationRhythmsToPublicationRhythmDTOs(Set<PublicationRhythm> publicationRhythms) {
        return MapperUtils.mapSet(publicationRhythms, this::PublicationRhythmToPublicationRhythmDTO);
    }

    public Set<PublicationRhythm> PublicationRhythmDTOsToPublicationRhythms(Set<PublicationRhythmDTO> publicationRhythmDTOs) {
        return MapperUtils.mapSet(publicationRhythmDTOs, this::PublicationRhythmDTOToPublicationRhythm);
    }
    public List<PublicationRhythmDTO> PublicationRhythmsToPublicationRhythmDTOs(List<PublicationRhythm> publicationRhythms) {
        return MapperUtils.mapList(publicationRhythms, this::PublicationRhythmToPublicationRhythmDTO);
    }

    public List<PublicationRhythm> PublicationRhythmDTOsToPublicationRhythms(List<PublicationRhythmDTO> publicationRhythmDTOs) {
        return MapperUtils.mapList(publicationRhythmDTOs, this::PublicationRhythmDTOToPublicationRhythm);
    }
}
