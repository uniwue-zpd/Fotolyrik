package de.uniwue.dachs.fotolyrik_backend.model;

public enum PersonSearchMatchMode {
    STARTS_WITH,
    CONTAINS,
    NOT_CONTAINS,
    ENDS_WITH,
    EQUALS,
    NOT_EQUALS;

    public boolean isNegative() {
        return this == NOT_CONTAINS || this == NOT_EQUALS;
    }

    public PersonSearchMatchMode positiveMode() {
        return switch (this) {
            case NOT_CONTAINS -> CONTAINS;
            case NOT_EQUALS -> EQUALS;
            default -> this;
        };
    }
}
