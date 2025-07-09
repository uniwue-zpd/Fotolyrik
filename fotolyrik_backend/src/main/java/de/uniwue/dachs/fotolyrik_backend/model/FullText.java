package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "full_text")
@Getter
@Setter
public class FullText extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "photopoem_id", nullable = false)
    private Photopoem photopoem;

    @Column(name = "full_text", length = 3000)
    private String full_text;

    @Column(name = "full_text_vector", columnDefinition = "tsvector", insertable = false, updatable = false)
    private String full_text_vector;

}
