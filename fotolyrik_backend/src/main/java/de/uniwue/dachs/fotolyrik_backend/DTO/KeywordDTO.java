package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordDTO extends BaseEntity {
    private Long id;
    private String value;
    private String gndId;
}
