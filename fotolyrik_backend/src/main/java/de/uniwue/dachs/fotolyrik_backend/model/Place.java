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
    @Column(name = "has_name", unique = true, nullable = false)
    private String name;

    @Column(name = "has_description")
    private String description;

    @Column(name = "has_longitude")
    private double longitude;

    @Column(name = "has_latitude")
    private double latitude;
}
