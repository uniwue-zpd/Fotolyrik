package de.uniwue.dachs.fotolyrik_backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GndSuggestionDTO {
    private String gndId;
    private String preferredName;
    private boolean exactMatch;
}
