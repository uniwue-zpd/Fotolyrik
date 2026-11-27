package de.uniwue.dachs.fotolyrik_backend.DTO;

import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelectionType;

public record PhotopoemSelectionRequest(Long photopoemId, PhotopoemSelectionType type) {
}
