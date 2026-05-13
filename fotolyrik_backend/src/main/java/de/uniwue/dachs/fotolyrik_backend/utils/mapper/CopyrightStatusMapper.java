package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.CopyrightStatusDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.CopyrightStatusPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.repository.CopyrightStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CopyrightStatusMapper {
    private final CopyrightStatusRepository copyrightStatusRepository;

    public CopyrightStatusMapper(CopyrightStatusRepository copyrightStatusRepository) {
        this.copyrightStatusRepository = copyrightStatusRepository;
    }

    public CopyrightStatus CopyrightStatusDTOToCopyrightStatus(CopyrightStatusDTO copyrightStatusDTO) {
        if (copyrightStatusDTO == null) return null;
        if (copyrightStatusDTO.getId() != null) {
            return copyrightStatusRepository.findById(copyrightStatusDTO.getId()).orElse(null);
        } else {
            CopyrightStatus copyrightStatus = new CopyrightStatus();
            copyrightStatus.setValue(copyrightStatusDTO.getValue());
            copyrightStatusRepository.save(copyrightStatus);
            return copyrightStatus;
        }
    }

    public CopyrightStatusDTO CopyrightStatusToCopyrightStatusDTO(CopyrightStatus copyrightStatus) {
        if (copyrightStatus == null) return null;
        CopyrightStatusDTO copyrightStatusDTO = new CopyrightStatusDTO();
        copyrightStatusDTO.setId(copyrightStatus.getId());
        copyrightStatusDTO.setValue(copyrightStatus.getValue());
        copyrightStatusDTO.setBaseEntityFields(copyrightStatus);
        return  copyrightStatusDTO;
    }

    public Set<CopyrightStatus> CopyrightStatusDTOsToCopyrightStatuses(Set<CopyrightStatusDTO> copyrightStatusDTOS) {
        return MapperUtils.mapSet(copyrightStatusDTOS, this::CopyrightStatusDTOToCopyrightStatus);
    }
    public Set<CopyrightStatusDTO> CopyrightStatusesToCopyrightStatusDTOs(Set<CopyrightStatus> copyrightStatuses) {
        return MapperUtils.mapSet(copyrightStatuses, this::CopyrightStatusToCopyrightStatusDTO);
    }
    public List<CopyrightStatus> CopyrightStatusDTOsToCopyrightStatuses(List<CopyrightStatusDTO> copyrightStatusDTOS) {
        return MapperUtils.mapList(copyrightStatusDTOS, this::CopyrightStatusDTOToCopyrightStatus);
    }

    public List<CopyrightStatusDTO> CopyrightStatusesToCopyrightStatusDTOs(List<CopyrightStatus> copyrightStatuses) {
        return MapperUtils.mapList(copyrightStatuses, this::CopyrightStatusToCopyrightStatusDTO);
    }


    public CopyrightStatus CopyrightStatusPreviewDTOToCopyrightStatus(CopyrightStatusPreviewDTO copyrightStatusPreviewDTO) {
        if (copyrightStatusPreviewDTO == null || copyrightStatusPreviewDTO.getId() == null) return null;
        return copyrightStatusRepository.findById(copyrightStatusPreviewDTO.getId()).orElse(null);
    }
    public CopyrightStatusPreviewDTO CopyrightStatusToCopyrightStatusPreviewDTO(CopyrightStatus keyword) {
        if (keyword == null) return null;
        CopyrightStatusPreviewDTO keywordPreviewDTO = new CopyrightStatusPreviewDTO();
        keywordPreviewDTO.setId(keyword.getId());
        keywordPreviewDTO.setValue(keyword.getValue());
        return  keywordPreviewDTO;
    }

    public Set<CopyrightStatus> CopyrightStatusPreviewDTOsToCopyrightStatuses(Set<CopyrightStatusPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapSet(keywordPreviewDTOs, this::CopyrightStatusPreviewDTOToCopyrightStatus);
    }
    public Set<CopyrightStatusPreviewDTO> CopyrightStatusesToCopyrightStatusPreviewDTOs(Set<CopyrightStatus> keywords) {
        return MapperUtils.mapSet(keywords, this::CopyrightStatusToCopyrightStatusPreviewDTO);
    }
    public List<CopyrightStatus> CopyrightStatusPreviewDTOsToCopyrightStatuses(List<CopyrightStatusPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapList(keywordPreviewDTOs, this::CopyrightStatusPreviewDTOToCopyrightStatus);
    }
    public List<CopyrightStatusPreviewDTO> CopyrightStatusesToCopyrightStatusPreviewDTOs(List<CopyrightStatus> keywords) {
        return MapperUtils.mapList(keywords, this::CopyrightStatusToCopyrightStatusPreviewDTO);
    }
}
