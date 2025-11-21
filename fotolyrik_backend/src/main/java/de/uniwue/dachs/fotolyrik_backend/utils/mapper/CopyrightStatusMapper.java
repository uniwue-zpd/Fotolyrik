package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.CopyrightStatusDTO;
import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.repository.CopyrightStatusRepository;
import org.springframework.stereotype.Component;

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
}
