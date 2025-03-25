package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photopoem")
@Getter
@Setter
public class Photopoem extends BaseEntity {
    @Column(name = "has_title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person author;

    @Column(name = "has_iiif_manifest")
    private String iiifManifest;

    //TODO: Add other required fields
}
