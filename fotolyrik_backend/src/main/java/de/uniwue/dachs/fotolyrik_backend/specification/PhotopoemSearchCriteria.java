package de.uniwue.dachs.fotolyrik_backend.specification;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Bundles all optional search and filter criteria for photopoems.
 * Each non-null (and, for strings, non-empty) value is applied as an AND-condition.
 */
public record PhotopoemSearchCriteria(
        String title,
        String subtitle,
        String altTitle,
        String series,
        Long volume,
        Long issue,
        String publicationDate,
        Long pubMediumId,
        String pubMedium,
        Long pubPlaceId,
        Long locationId,
        Long authorId,
        String author,
        Long photographerId,
        String photographer,
        Long depictedPersonId,
        String depictedPerson,
        Long participantId,
        String participant,
        Long otherContributorId,
        String otherContributor,
        Long themeId,
        String theme,
        Long imageMotifId,
        String imageMotif,
        Long copyrightStatusImageId,
        String copyrightStatusImage,
        Long copyrightStatusTextId,
        String copyrightStatusText,
        Long languageId,
        String language
) {
    /**
     * @return {@code true} if no criterion is set
     */
    public boolean isEmpty() {
        return Stream.<Object>of(
                title, subtitle, altTitle, series, volume, issue, publicationDate,
                pubMediumId, pubMedium, pubPlaceId, locationId, authorId, author,
                photographerId, photographer, depictedPersonId, depictedPerson,
                participantId, participant, otherContributorId, otherContributor,
                themeId, theme, imageMotifId, imageMotif,
                copyrightStatusImageId, copyrightStatusImage,
                copyrightStatusTextId, copyrightStatusText, languageId, language
        ).filter(Objects::nonNull).allMatch(v -> v instanceof String s && s.isEmpty());
    }
}
