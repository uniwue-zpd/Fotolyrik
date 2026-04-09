package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "LOCATION")
@Getter
@Setter
public class Location extends BaseEntity {
    private String name;
    private String description;
}
