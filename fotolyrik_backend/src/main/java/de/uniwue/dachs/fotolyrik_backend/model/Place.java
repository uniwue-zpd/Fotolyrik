package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter
@Setter
public class Place extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    private double longitude;

    private double latitude;
}
