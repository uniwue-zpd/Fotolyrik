package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photopoem")
@Getter
@Setter
public class Photopoem extends BaseEntity {
    @Column(name = "has_title", nullable = false)
    private String title;

    @Column(name = "has_volume")
    private Long volume;

    @Column(name = "has_issue")
    private Long issue;

    @Column(name = "has_pagenumber")
    private Long pageNumber;

    @Column(name = "has_publication_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate publicationDate;

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private PubMedium publicationMedium;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Person photographer;

    @Column(name = "has_link")
    private String link;

    @Column(name = "has_iiif_manifest")
    private String iiifManifest;

    //TODO: Add other required fields
    //TODO: Rename fields according to one general scheme
}
