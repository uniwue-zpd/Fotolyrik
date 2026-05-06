package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.CopyrightStatusDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.repository.CopyrightStatusRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
}
