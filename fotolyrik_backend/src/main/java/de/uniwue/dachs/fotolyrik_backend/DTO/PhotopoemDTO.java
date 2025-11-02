package de.uniwue.dachs.fotolyrik_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private Long pageCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate publicationDate;

    private PubMediumDTO publicationMedium;
    private Set<PersonDTO> authors = new HashSet<>();
    private Set<PersonDTO> photographers = new HashSet<>();
    private Set<PersonDTO> otherContributors = new HashSet<>();
    private Set<KeywordDTO> themes = new HashSet<>();
    private Set<KeywordDTO> imageMotifs = new HashSet<>();
    private String form;
    private String link;
    private String iiifManifest;
    private Set<FileDTO> images = new HashSet<>();
    private String copyrightStatusImage;
    private String copyrightStatusText;
    private Set<String> languages = new HashSet<>();
}
