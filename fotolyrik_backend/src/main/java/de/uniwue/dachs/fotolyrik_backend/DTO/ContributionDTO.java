package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.ContributionRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContributionDTO {
    private Long id;
    private PersonPreviewDTO contributor;
    private String pseudonym;
    private ContributionRole role;
}
