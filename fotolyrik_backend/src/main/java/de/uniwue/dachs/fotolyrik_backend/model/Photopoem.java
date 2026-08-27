package de.uniwue.dachs.fotolyrik_backend.model;

import de.uniwue.dachs.fotolyrik_backend.utils.enums.AccessLevel;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "photopoem")
@Getter
@Setter
public class Photopoem extends BaseEntity {
    private String title;

    private String subtitle;

    private String altTitle;

    private String series;

    private Long volume;

    private Long issue;

    private String pageNumber;

    private Long manifestPageNumber;

    private Long pageCount;

    private String pictureCount;

    private String publicationDate;

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private PubMedium publicationMedium;

    @ManyToMany
    @JoinTable(
            name = "photopoem_locations",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> foundIn = new HashSet<>();

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
            name = "photopoem_depicted_people",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> depictedPeople = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "photopoem_other_contributors",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> otherContributors = new HashSet<>();

    @OneToMany(mappedBy = "workContributedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contribution> contributions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "photopoem_themes",
            joinColumns = @JoinColumn(name = "photopoem_id",
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (photopoem_id) REFERENCES photopoem(id) ON DELETE CASCADE")),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Keyword> themes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "photopoem_image_motifs",
            joinColumns = @JoinColumn(name = "photopoem_id",
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (photopoem_id) REFERENCES photopoem(id) ON DELETE CASCADE")),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Keyword> imageMotifs = new HashSet<>();

    private String form;

    private String link;

    private String iiifManifest;

    @OneToMany
    @JoinColumn(name = "photopoem_id")
    private Set<File> images = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'INTERNAL'")
    private AccessLevel imagesVisible = AccessLevel.INTERNAL;

    @ManyToOne
    @JoinColumn(name = "copyrightstatus_image_id")
    private CopyrightStatus copyrightStatusImage;

    @ManyToOne
    @JoinColumn(name = "copyrightstatus_text_id")
    private CopyrightStatus copyrightStatusText;

    @ManyToMany
    @JoinTable(
            name = "photopoem_languages",
            joinColumns = @JoinColumn(name = "photopoem_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages = new HashSet<>();

    public void updateContributions(Set<Contribution> newContributions) {
        this.contributions.clear();
        if (newContributions != null) {
            this.contributions.addAll(newContributions);
        }
    }
}
