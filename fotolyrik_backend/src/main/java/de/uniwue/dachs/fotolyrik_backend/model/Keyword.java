package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "keyword")
@Getter
@Setter
public class Keyword extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String value;
    private String gndId;
}
