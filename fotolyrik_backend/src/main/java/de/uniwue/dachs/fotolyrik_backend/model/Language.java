package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
public class Language extends BaseEntity {
    private String name;
    private String isoDesignation;
}
