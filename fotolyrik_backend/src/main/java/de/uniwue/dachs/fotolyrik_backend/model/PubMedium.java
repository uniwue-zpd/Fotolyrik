package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pub_medium")
@Getter
@Setter
public class PubMedium extends BaseEntity {
    @Column(nullable = false)
    private String title;

    private String subtitle;

    @ManyToMany
    @JoinTable(
            name = "pub_medium_place",
            joinColumns = @JoinColumn(name = "pub_medium_id"),
            inverseJoinColumns = @JoinColumn(name = "pub_place_id")
    )
    private Set<Place> publicationPlaces = new HashSet<>();

    private String publisher;

    private String pubRhytm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year startYear;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year endYear;

    private Integer amountVolumes;

    private Integer amountIssues;

    private String zdbId;
}
