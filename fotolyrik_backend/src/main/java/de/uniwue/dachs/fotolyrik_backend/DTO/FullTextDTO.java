package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PhotopoemPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullTextDTO extends BaseEntity {
    private PhotopoemPreviewDTO photopoem;
    private String fullText;
}
