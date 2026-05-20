package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO extends BaseEntity {
    private String originalFilename;
    private String filename;
}
