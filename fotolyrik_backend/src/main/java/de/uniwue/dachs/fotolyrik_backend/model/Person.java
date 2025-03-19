package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person extends BaseEntity {
    @Column(name = "has_first_name")
    private String firstName;

    @Column(name = "has_last_name")
    private String lastName;

    @Column(name = "has_pseudonym")
    private String pseudonym;

    @Column(name = "has_birth_year", length = 4)
    private Integer birthYear;

    @Column(name = "has_death_year", length = 4)
    private Integer deathYear;

    @Column(name = "has_sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    public enum Sex {
        MALE, FEMALE
    }
}
