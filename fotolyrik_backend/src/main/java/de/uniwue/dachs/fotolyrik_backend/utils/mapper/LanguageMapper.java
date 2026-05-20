package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.LanguageDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.LanguagePreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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


    public LanguageDTO LanguageToLanguageDTO(Language language) {
        if (language == null) return null;
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setId(language.getId());
        languageDTO.setName(language.getName());
        languageDTO.setIsoDesignation(language.getIsoDesignation());
        language.setBaseEntityFields(languageDTO);
        return languageDTO;
    }
    public Set<LanguageDTO> LanguagesToLanguageDTOs(Set<Language> languages) {
        return MapperUtils.mapSet(languages, this::LanguageToLanguageDTO);
    }
    public Set<Language> LanguageDTOsToLanguages(Set<LanguageDTO> languageDTOs) {
        return MapperUtils.mapSet(languageDTOs, this::LanguageDTOToLanguage);
    }
    public List<LanguageDTO> LanguagesToLanguageDTOs(List<Language> languages) {
        return MapperUtils.mapList(languages, this::LanguageToLanguageDTO);
    }
    public List<Language> LanguageDTOsToLanguages(List<LanguageDTO> languageDTOs) {
        return MapperUtils.mapList(languageDTOs, this::LanguageDTOToLanguage);
    }

    public Language LanguagePreviewDTOToLanguage(LanguagePreviewDTO languagePreviewDTO) {
        if (languagePreviewDTO == null || languagePreviewDTO.getId() == null) return null;
        return languageRepository.findById(languagePreviewDTO.getId()).orElse(null);
    }

    public LanguagePreviewDTO LanguageToLanguagePreviewDTO(Language language) {
        if (language == null) return null;
        LanguagePreviewDTO languagePreviewDTO = new LanguagePreviewDTO();
        languagePreviewDTO.setId(language.getId());
        languagePreviewDTO.setName(language.getName());
        return  languagePreviewDTO;
    }

    public Set<Language> LanguagePreviewDTOsToLanguages(Set<LanguagePreviewDTO> languagePreviewDTOs) {
        return MapperUtils.mapSet(languagePreviewDTOs, this::LanguagePreviewDTOToLanguage);
    }
    public Set<LanguagePreviewDTO> LanguagesToLanguagePreviewDTOs(Set<Language> languages) {
        return MapperUtils.mapSet(languages, this::LanguageToLanguagePreviewDTO);
    }
    public List<Language> LanguagePreviewDTOsToLanguages(List<LanguagePreviewDTO> languagePreviewDTOs) {
        return MapperUtils.mapList(languagePreviewDTOs, this::LanguagePreviewDTOToLanguage);
    }
    public List<LanguagePreviewDTO> LanguagesToLanguagePreviewDTOs(List<Language> languages) {
        return MapperUtils.mapList(languages, this::LanguageToLanguagePreviewDTO);
    }
}
