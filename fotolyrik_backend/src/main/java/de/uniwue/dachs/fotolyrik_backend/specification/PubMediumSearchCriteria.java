package de.uniwue.dachs.fotolyrik_backend.specification;

/**
 * Bundles all optional search and filter criteria for publication media.
 * Each non-null (and, for strings, non-blank) value is applied as an AND-condition.
 *
 * @param query matches title, subtitle or "title subtitle" combined
 */
public record PubMediumSearchCriteria(
        String query,
        String title,
        String subtitle,
        Long pubPlaceId,
        String pubPlace,
        Long publisherId,
        String publisher,
        Long pubRhythmId,
        String pubRhythm,
        String editorialOffice,
        Integer startYear,
        Integer endYear,
        Long amountVolumes,
        Long amountIssues,
        String zdbId
) {
}
