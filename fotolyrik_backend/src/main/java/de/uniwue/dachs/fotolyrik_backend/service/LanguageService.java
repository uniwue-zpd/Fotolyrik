package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * @return a {@link List} of available {@link Language} objects
     */
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    /**
     * @param id ID of the {@link Language} to be found
     * @return an {@link Optional} object of the {@link Language}
     */
    public Optional<Language> getLanguageById(Long id) {
        return languageRepository.findById(id);
    }

    /**
     * @param language a {@link Language} object to be saved
     * @return saved {@link Language} entry
     */
    @Transactional
    public Language createLanguage(Language language) {
        return languageRepository.save(language);
    }

    /**
     * @param id ID of the {@link Language} object to be updated
     * @param language {@link Language} entry with updated data
     * @return updated {@link Language} object
     */
    @Transactional
    public Language updateLanguage(Long id, Language language) {
        return languageRepository.findById(id)
                .map(entity -> {
                    entity.mapBaseEntityFields(language);
                    entity.setName(language.getName());
                    entity.setIsoDesignation(language.getIsoDesignation());
                    return languageRepository.save(entity);
                }).orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
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
