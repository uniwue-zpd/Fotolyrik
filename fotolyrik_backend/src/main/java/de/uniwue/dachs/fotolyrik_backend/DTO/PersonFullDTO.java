package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonFullDTO extends BaseEntity {
    private String firstName;
    private String lastName;
    private String studioName;
    private String fullName;
    private List<String> pseudonyms;
    private Integer birthYear;
    private Integer deathYear;
    private String sex;
    private String gndId;
    private String notes;
    private FileDTO image;
}
