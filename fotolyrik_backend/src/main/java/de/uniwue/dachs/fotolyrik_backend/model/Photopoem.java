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

    @Column(name = "volume")
    private Long volume;

    @Column(name = "issue")
    private Long issue;

    @Column(name = "page_number")
    private Long page_number;

    @Column(name = "page_count")
    private Long page_count;

    @Column(name = "publication_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate publication_date;

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private PubMedium publication_medium;

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
    private Set<Person> other_contributors = new HashSet<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "photopoem_themes", joinColumns = @JoinColumn(name = "photopoem_id"))
    private List<String> themes = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "photopoem_topics", joinColumns = @JoinColumn(name = "photopoem_id"))
    private List<String> topics = new ArrayList<>();

    @Column(name = "form")
    private String form;

    @Column(name = "link")
    private String link;

    @Column(name = "iiif_manifest")
    private String iiif_manifest;

    @OneToMany
    @JoinColumn(name = "photopoem_id")
    private Set<File> images = new HashSet<>();

    @Column(name = "copyright_status_image")
    private String copyright_status_image;

    @Column(name = "copyright_status_text")
    private String copyright_status_text;

    @Column(name = "language")
    private String language;

    //TODO: Add other required fields
}
