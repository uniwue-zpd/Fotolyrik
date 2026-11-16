package de.uniwue.dachs.fotolyrik_backend.specification;

/**
 * Bundles all optional search and filter criteria for keywords.
 * Each non-null (and, for strings, non-blank) value is applied as an AND-condition.
 */
public record KeywordSearchCriteria(
        String value,
        String gndId
) {
}
