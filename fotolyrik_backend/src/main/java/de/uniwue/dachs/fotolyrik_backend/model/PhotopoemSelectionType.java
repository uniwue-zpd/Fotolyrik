package de.uniwue.dachs.fotolyrik_backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PhotopoemSelectionType {
    HIGHLIGHT,
    CAROUSEL;

    @JsonCreator
    public static PhotopoemSelectionType fromValue(String value) {
        return valueOf(value);
    }
}
