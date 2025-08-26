package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photopoem")
@Getter
@Setter
public class Photopoem extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    private String subtitle;

    private String altTitle;

    private Long volume;

    private Long issue;

    private Long pageNumber;

    private Long pageCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate publicationDate;

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private PubMedium publicationMedium;

    @ManyToMany
    @JoinTable(
            name = "photopoem_authors",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "photopoem_photographers",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> photographers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "photopoem_other_contributors",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> otherContributors = new HashSet<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "photopoem_themes", joinColumns = @JoinColumn(name = "photopoem_id"))
    private List<String> themes = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "photopoem_topics", joinColumns = @JoinColumn(name = "photopoem_id"))
    private List<String> topics = new ArrayList<>();

    private String form;

    private String link;

    private String iiifManifest;

    @OneToMany
    @JoinColumn(name = "photopoem_id")
    private Set<File> images = new HashSet<>();

    private String copyrightStatusImage;

    private String copyrightStatusText;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "photopoem_languages", joinColumns = @JoinColumn(name = "photopoem_id"))
    private List<String> languages;
}
