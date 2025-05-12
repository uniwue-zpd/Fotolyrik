package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person extends BaseEntity {
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "pseudonym")
    private String pseudonym;

    @Column(name = "birth_year", length = 4)
    private Integer birth_year;

    @Column(name = "death_year", length = 4)
    private Integer death_year;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Transient
    public String getFull_name() {
        return (first_name != null ? first_name : "") + " " +
                (last_name != null ? last_name : "");
    }

    public enum Sex {
        MALE, FEMALE
    }
}
