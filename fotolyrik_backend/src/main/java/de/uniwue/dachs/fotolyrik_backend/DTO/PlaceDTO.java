package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDTO extends BaseEntity {
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
}
