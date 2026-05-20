package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherDTO extends BaseEntity {
    private String name;
    private String description;
}
