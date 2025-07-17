package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person extends BaseEntity {
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "person_pseudonymes", joinColumns = @JoinColumn(name = "person_id"))
    private List<String> pseudonyms = new ArrayList<>();

    @Column(name = "birth_year", length = 4)
    private Integer birth_year;

    @Column(name = "death_year", length = 4)
    private Integer death_year;

    @Column(name = "sex")
    private String sex;

    @Column(name = "gnd_id")
    private String gnd_id;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File image;

    @Transient
    public String getFull_name() {
        return (first_name != null ? first_name + " " : "") +
                (last_name != null ? last_name : "");
    }
}
