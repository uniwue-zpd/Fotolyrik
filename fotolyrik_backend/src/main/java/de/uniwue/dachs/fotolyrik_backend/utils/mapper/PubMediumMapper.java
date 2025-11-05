package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.stereotype.Component;

@Component
public class PubMediumMapper {
    private final PubMediumRepository pubMediumRepository;

    public  PubMediumMapper(PubMediumRepository pubMediumRepository) {
        this.pubMediumRepository = pubMediumRepository;
    }

    public PubMedium PubMediumDTOToPubmedium(PubMediumDTO pubMediumDTO) {
        if (pubMediumDTO == null) return null;
        if (pubMediumDTO.getId() != null) {
            return pubMediumRepository.findById(pubMediumDTO.getId()).orElse(null);
        } else {
            PubMedium pubMedium = new PubMedium();
            pubMedium.setTitle(pubMediumDTO.getTitle());
            pubMediumRepository.save(pubMedium);
            return pubMedium;
        }
    }

    public PubMediumDTO PubMediumToPubmediumDTO(PubMedium pubMedium) {
        if (pubMedium == null) return null;
        PubMediumDTO pubMediumDTO = new PubMediumDTO();
        pubMediumDTO.setId(pubMedium.getId());
        pubMediumDTO.setTitle(pubMedium.getTitle());
        return  pubMediumDTO;
    }
}
