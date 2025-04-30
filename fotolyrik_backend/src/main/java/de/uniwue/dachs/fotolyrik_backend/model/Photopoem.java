package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
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

    @Column(name = "publication_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate publication_date;

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private PubMedium publication_medium;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Person photographer;

    @Column(name = "link")
    private String link;

    @Column(name = "iiif_manifest")
    private String iiif_manifest;

    @OneToMany
    @JoinColumn(name = "photopoem_id")
    private Set<File> images = new HashSet<>();

    //TODO: Add other required fields
}
