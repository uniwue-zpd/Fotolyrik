package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.LanguageDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LanguageMapper {
    private final LanguageRepository languageRepository;

    public LanguageMapper(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public Language LanguageDTOToLanguage(LanguageDTO languageDTO) {
        if (languageDTO == null) return null;
        if (languageDTO.getId() != null) {
            return languageRepository.findById(languageDTO.getId()).orElse(null);
        } else {
            Language language = new Language();
            language.setName(languageDTO.getName());
            language.setIsoDesignation(languageDTO.getIsoDesignation());
            languageRepository.save(language);
            return language;
        }
    }

    public Set<Language> LanguageDTOsToLanguages(Set<LanguageDTO> languageDTOs) {
        if (languageDTOs.isEmpty()) return Collections.emptySet();
        return languageDTOs.stream()
                .map(this::LanguageDTOToLanguage)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public LanguageDTO LanguageToLanguageDTO(Language language) {
        if (language == null) return null;
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setId(language.getId());
        languageDTO.setName(language.getName());
        languageDTO.setIsoDesignation(language.getIsoDesignation());
        return languageDTO;
    }

    public Set<LanguageDTO> LanguagesToLanguageDTOs(Set<Language> languages) {
        if (languages.isEmpty()) return Collections.emptySet();
        return languages.stream()
                .map(this::LanguageToLanguageDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
