package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "photopoem_selection")
@Getter
@Setter
public class PhotopoemSelection extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "photopoem_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Photopoem photopoem;

    @Enumerated(EnumType.STRING)
    @Column(name = "selection_type", nullable = false)
    private PhotopoemSelectionType type;
}
