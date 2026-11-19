package de.uniwue.dachs.fotolyrik_backend.specification;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Bundles all optional search and filter criteria for persons.
 * Each non-null (and, for strings, non-blank) value is applied as an AND-condition.
 */
public record PersonSearchCriteria(
        String name,
        String studioName,
        String pseudonym,
        String gndId,
        String sex,
        Integer birthYear,
        Integer birthYearFrom,
        Integer birthYearTo,
        Integer deathYear,
        Integer deathYearFrom,
        Integer deathYearTo
) {
    /**
     * @return {@code true} if no criterion is set
     */
    public boolean isEmpty() {
        return Stream.<Object>of(
                name, studioName, pseudonym, gndId, sex,
                birthYear, birthYearFrom, birthYearTo, deathYear, deathYearFrom, deathYearTo
        ).filter(Objects::nonNull).allMatch(v -> v instanceof String s && s.isBlank());
    }
}
