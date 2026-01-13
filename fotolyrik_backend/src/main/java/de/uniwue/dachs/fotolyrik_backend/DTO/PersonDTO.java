package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String fullName;
    private List<String> pseudonyms;
}
