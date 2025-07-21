package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String firstName;

    private String lastName;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "person_pseudonymes", joinColumns = @JoinColumn(name = "person_id"))
    private List<String> pseudonyms = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Integer birthYear;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Integer deathYear;

    private String sex;

    private String gndId;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File image;

    @Transient
    public String getFullName() {
        return (firstName != null ? firstName + " " : "") +
                (lastName != null ? lastName : "");
    }
}
