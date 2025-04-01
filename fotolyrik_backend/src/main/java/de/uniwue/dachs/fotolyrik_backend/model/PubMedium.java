package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pub_medium")
@Getter
@Setter
public class PubMedium extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @ManyToMany
    @JoinTable(
            name = "pub_medium_place",
            joinColumns = @JoinColumn(name = "pub_medium_id"),
            inverseJoinColumns = @JoinColumn(name = "pub_place_id")
    )
    private Set<Place> publicationPlaces = new HashSet<>();

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "pub_rhytm")
    @Enumerated(EnumType.STRING)
    private PublicationRhytm pub_rhytm;

    @Column(name = "start_year")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private LocalDate startYear;

    @Column(name = "end_year")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private LocalDate endYear;

    @Column(name = "amount_volumes")
    private Integer amount_volumes;

    @Column(name = "amount_issues")
    private Integer amount_issues;

    public enum PublicationRhytm {
        W, M, HM
    }
}
