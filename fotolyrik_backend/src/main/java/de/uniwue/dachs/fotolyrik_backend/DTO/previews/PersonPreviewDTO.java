package de.uniwue.dachs.fotolyrik_backend.DTO.previews;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonPreviewDTO {
    private Long id;
    private String fullName;
    private String studioName;
    private List<String> pseudonyms;
}
