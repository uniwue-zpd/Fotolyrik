package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.CopyrightStatusDTO;
import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.repository.CopyrightStatusRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.CopyrightStatusMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CopyrightStatusService {
    private final CopyrightStatusRepository copyrightStatusRepository;
    private final CopyrightStatusMapper copyrightStatusMapper;

    public CopyrightStatusService(CopyrightStatusRepository copyrightStatusRepository, CopyrightStatusMapper copyrightStatusMapper) {
        this.copyrightStatusRepository = copyrightStatusRepository;
        this.copyrightStatusMapper = copyrightStatusMapper;
    }

    /**
     * @return a {@link List} of all available {@link CopyrightStatus} entries as {@link CopyrightStatusDTO}
     */
    public List<CopyrightStatusDTO> getAllCopyrightStatuses() {
        return copyrightStatusRepository.findAll()
                .stream()
                .map(copyrightStatusMapper::CopyrightStatusToCopyrightStatusDTO)
                .collect(Collectors.toList());
    }

    /**
     * @param id ID of the {@link CopyrightStatus} to be found
     * @return an {@link Optional} object of the {@link CopyrightStatus} as {@link CopyrightStatusDTO}
     */
    public Optional<CopyrightStatusDTO> getCopyrightStatusById(Long id) {
        return copyrightStatusRepository.findById(id)
                .map(copyrightStatusMapper::CopyrightStatusToCopyrightStatusDTO);
    }


    /**
     * @param copyrightStatusDTO a {@link CopyrightStatusDTO} representation of the {@link CopyrightStatus} object to be saved
     * @return {@link CopyrightStatusDTO} of the saved {@link CopyrightStatus} entry
     */

    @Transactional
    public CopyrightStatusDTO createCopyrightStatus(CopyrightStatusDTO copyrightStatusDTO) {
        var entity = copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(copyrightStatusDTO);
        var savedEntity = copyrightStatusRepository.save(entity);
        return copyrightStatusMapper.CopyrightStatusToCopyrightStatusDTO(savedEntity);
    }

    /**
     * @param id ID of the {@link CopyrightStatus} object to be updated
     * @param copyrightStatusDTO {@link CopyrightStatusDTO} entry with updated data
     * @return {@link CopyrightStatusDTO} of updated {@link CopyrightStatus} object
     */
    @Transactional
    public CopyrightStatusDTO updateCopyrightStatus(Long id, CopyrightStatusDTO copyrightStatusDTO) {
        var copyrightStatus = copyrightStatusMapper.CopyrightStatusDTOToCopyrightStatus(copyrightStatusDTO);
        return copyrightStatusRepository.findById(id)
                .map(entity -> {
                    entity.mapBaseEntityFields(copyrightStatus);
                    entity.setValue(copyrightStatus.getValue());
                    entity.setDescription(copyrightStatus.getDescription());
                    var savedEntity = copyrightStatusRepository.save(entity);
                    return copyrightStatusMapper.CopyrightStatusToCopyrightStatusDTO(savedEntity);
                }).orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id of the {@link CopyrightStatus} object to be deleted
     */
    @Transactional
    public void deleteCopyrightStatusById(Long id) {
        if (!copyrightStatusRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' does not exist");
        }
    }
}
