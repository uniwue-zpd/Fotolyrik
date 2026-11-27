package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelectionType;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PhotopoemSelectionDTO extends BaseEntity {
    private Long photopoemId;
    private PhotopoemSelectionType type;
}
