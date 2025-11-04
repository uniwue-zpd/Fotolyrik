package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
    private Long id;
    private String originalFilename;
    private String filename;
}
