package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IDSliceDTO {
    private Long previous;
    private Long current;
    private Long next;
}
