package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.CopyrightStatus;
import de.uniwue.dachs.fotolyrik_backend.repository.CopyrightStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CopyrightStatusService {
    private final CopyrightStatusRepository copyrightStatusRepository;

    public CopyrightStatusService(CopyrightStatusRepository copyrightStatusRepository) {
        this.copyrightStatusRepository = copyrightStatusRepository;
    }

    /**
     * @return a {@link List} of all available {@link CopyrightStatus} entries
     */
    public List<CopyrightStatus> getAllCopyrightStatuses() {
        return copyrightStatusRepository.findAll();
    }

    /**
     * @param id ID of the {@link CopyrightStatus} to be found
     * @return an {@link Optional} object of the {@link CopyrightStatus}
     */
    public Optional<CopyrightStatus> getCopyrightStatusById(Long id) {
        return copyrightStatusRepository.findById(id);
    }

    /**
     * @param copyrightStatus a {@link CopyrightStatus} object to be saved
     * @return saved {@link CopyrightStatus} entry
     */
    @Transactional
    public CopyrightStatus createCopyrightStatus(CopyrightStatus copyrightStatus) {
        return copyrightStatusRepository.save(copyrightStatus);
    }

    /**
     * @param id ID of the {@link CopyrightStatus} object to be updated
     * @param copyrightStatus {@link CopyrightStatus} entry with updated data
     * @return updated {@link CopyrightStatus} object
     */
    @Transactional
    public CopyrightStatus updateCopyrightStatus(Long id, CopyrightStatus copyrightStatus) {
        return copyrightStatusRepository.findById(id)
                .map(entity -> {
                    entity.setValue(copyrightStatus.getValue());
                    entity.setDescription(copyrightStatus.getDescription());
                    return copyrightStatusRepository.save(entity);
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
