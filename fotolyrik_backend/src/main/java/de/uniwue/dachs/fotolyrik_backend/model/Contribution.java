package de.uniwue.dachs.fotolyrik_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "contribution")
@Getter
@Setter
public class Contribution extends BaseEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person contributor;

    private String pseudonym;
    @ManyToOne
    @JoinColumn(name = "photopoem_id", nullable = false)
    private Photopoem workContributedTo;
    @Enumerated(EnumType.STRING)
    private ContributionRole role;
}
