package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "copyright_status")
@Getter
@Setter
public class CopyrightStatus extends BaseEntity {
    private String value;
    private String description;
}
