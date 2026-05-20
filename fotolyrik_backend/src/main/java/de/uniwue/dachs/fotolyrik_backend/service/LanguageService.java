package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.LanguageDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.repository.LanguageRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.LanguageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    /**
     * @return a {@link List} of available {@link Language} as {@link LanguageDTO} objects
     */
    public List<LanguageDTO> getAllLanguages() {
        return languageMapper.LanguagesToLanguageDTOs(languageRepository.findAll());
    }

    /**
     * @param id ID of the {@link Language} to be found
     * @return an {@link Optional} object of the {@link Language} as {@link LanguageDTO}
     */
    public Optional<LanguageDTO> getLanguageById(Long id) {
        return languageRepository.findById(id).map(languageMapper::LanguageToLanguageDTO);
    }

    /**
     * @param languageDTO a {@link LanguageDTO} object to be saved
     * @return {@link LanguageDTO} of saved {@link Language} entry
     */
    @Transactional
    public LanguageDTO createLanguage(LanguageDTO languageDTO) {
        var entity = languageMapper.LanguageDTOToLanguage(languageDTO);
        var savedEntity = languageRepository.save(entity);
        return languageMapper.LanguageToLanguageDTO(savedEntity);
    }

    /**
     * @param id ID of the {@link Language} object to be updated
     * @param languageDTO {@link LanguageDTO} entry with updated data
     * @return updated {@link LanguageDTO} object
     */
    @Transactional
    public LanguageDTO updateLanguage(Long id, LanguageDTO languageDTO) {
        return languageRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(languageDTO);
                    entity.setName(languageDTO.getName());
                    entity.setIsoDesignation(languageDTO.getIsoDesignation());
                    return languageRepository.save(entity);
                }).map(languageMapper::LanguageToLanguageDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * @param id of the {@link Language} object to be deleted
     */
    @Transactional
    public void deleteLanguageById(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' does not exist");
        }
        languageRepository.deleteById(id);
    }
}
