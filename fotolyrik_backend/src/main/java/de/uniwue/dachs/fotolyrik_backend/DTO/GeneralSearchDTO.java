package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneralSearchDTO {
    private Long id;
    private String title;
    private String type;
}
