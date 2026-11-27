package de.uniwue.dachs.fotolyrik_backend.specification;

/**
 * Bundles all optional search and filter criteria for publishers.
 * Each non-null (and, for strings, non-blank) value is applied as an AND-condition.
 */
public record PublisherSearchCriteria(
        String name
) {
}
