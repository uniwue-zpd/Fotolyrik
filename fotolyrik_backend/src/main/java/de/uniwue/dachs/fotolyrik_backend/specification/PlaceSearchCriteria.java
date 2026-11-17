package de.uniwue.dachs.fotolyrik_backend.specification;

/**
 * Bundles all optional search and filter criteria for places.
 * Each non-null (and, for strings, non-blank) value is applied as an AND-condition.
 */
public record PlaceSearchCriteria(
        String name
) {
}
