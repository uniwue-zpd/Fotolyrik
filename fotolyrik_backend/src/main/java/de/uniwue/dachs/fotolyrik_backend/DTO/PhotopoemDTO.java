package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PhotopoemDTO extends BaseEntity {
    private String title;
    private String subtitle;
    private String altTitle;
    private Long volume;
    private Long issue;
    private Long pageNumber;
    private Long manifestPageNumber;
    private Long pageCount;
    private String publicationDate;
    private PubMediumPreviewDTO publicationMedium;
    private Set<PersonDTO> authors = new HashSet<>();
    private Set<PersonDTO> photographers = new HashSet<>();
    private Set<PersonDTO> otherContributors = new HashSet<>();
    private Set<KeywordDTO> themes = new HashSet<>();
    private Set<KeywordDTO> imageMotifs = new HashSet<>();
    private String form;
    private String link;
    private String iiifManifest;
    private Set<FileDTO> images = new HashSet<>();
    private CopyrightStatusDTO copyrightStatusImage;
    private CopyrightStatusDTO copyrightStatusText;
    private Set<LanguageDTO> languages = new HashSet<>();
}
