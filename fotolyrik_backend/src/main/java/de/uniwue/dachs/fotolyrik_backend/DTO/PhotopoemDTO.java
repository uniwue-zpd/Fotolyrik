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
    private String pageNumber;
    private Long manifestPageNumber;
    private Long pageCount;
    private String pictureCount;
    private String publicationDate;
    private PubMediumPreviewDTO publicationMedium;
    private Set<PersonPreviewDTO> authors = new HashSet<>();
    private Set<PersonPreviewDTO> photographers = new HashSet<>();
    private Set<PersonPreviewDTO> otherContributors = new HashSet<>();
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
