package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
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

    @ManyToOne
    @JoinColumn(name = "pub_medium_id")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "pub_medium_rhythm",
            joinColumns = @JoinColumn(name = "pub_medium_id"),
            inverseJoinColumns = @JoinColumn(name = "pub_rhythm_id")
    )
    private Set<PublicationRhythm> pubRhytms = new HashSet<>();

    private String editorialOffice;

    @JdbcTypeCode(Types.SMALLINT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year startYear;

    @JdbcTypeCode(Types.SMALLINT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Year endYear;

    private Long amountVolumes;

    private Long amountIssues;

    private String zdbId;

    private String notes;
}
