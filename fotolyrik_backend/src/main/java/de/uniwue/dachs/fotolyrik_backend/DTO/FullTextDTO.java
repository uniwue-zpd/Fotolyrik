package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullTextDTO {
    private Long id;
    private PhotopoemPreviewDTO photopoem;
    private String fullText;
}
