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

    @Column(columnDefinition = "TEXT")
    private String fullText;
}
