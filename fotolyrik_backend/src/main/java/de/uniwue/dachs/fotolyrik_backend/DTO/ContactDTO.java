package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
    private String name;
    private String appellation;
    private String email;
    private String subject;
    private String message;
}
