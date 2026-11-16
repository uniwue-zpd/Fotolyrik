package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;
import java.util.Locale;
import java.util.function.BiFunction;

/**
 * Specifications for filtering {@link PubMedium} entities based on various attributes.
 */
public final class PubMediumSpecification {
    private PubMediumSpecification() {}

    /**
     * Matches publication media whose title, subtitle or combined "title subtitle"
     * contains the given value (case-insensitive).
     */
    public static Specification<PubMedium> hasTitleOrSubtitle(String query) {
        String pattern = containsPattern(query);
        return (root, q, cb) -> {
            Expression<String> title = cb.coalesce(root.get("title"), "");
            Expression<String> subtitle = cb.coalesce(root.get("subtitle"), "");
            Expression<String> combined = cb.trim(cb.concat(cb.concat(title, " "), subtitle));
            return cb.or(
                    like(cb, title, pattern),
                    like(cb, subtitle, pattern),
                    like(cb, combined, pattern)
            );
        };
    }

    public static Specification<PubMedium> hasTitle(String title) {
        return containsText("title", title);
    }

    public static Specification<PubMedium> hasSubtitle(String subtitle) {
        return containsText("subtitle", subtitle);
    }

    public static Specification<PubMedium> hasEditorialOffice(String editorialOffice) {
        return containsText("editorialOffice", editorialOffice);
    }

    public static Specification<PubMedium> hasZdbId(String zdbId) {
        return containsText("zdbId", zdbId);
    }

    public static Specification<PubMedium> hasPubPlaceId(Long pubPlaceId) {
        return placeExists((cb, place) -> cb.equal(place.get("id"), pubPlaceId));
    }

    public static Specification<PubMedium> hasPubPlace(String pubPlace) {
        String pattern = containsPattern(pubPlace);
        return placeExists((cb, place) -> like(cb, place.get("name"), pattern));
    }

    public static Specification<PubMedium> hasPublisherId(Long publisherId) {
        return (root, query, cb) -> cb.equal(root.get("publisher").get("id"), publisherId);
    }

    public static Specification<PubMedium> hasPublisher(String publisher) {
        String pattern = containsPattern(publisher);
        return (root, query, cb) -> like(cb, root.join("publisher", JoinType.LEFT).get("name"), pattern);
    }

    public static Specification<PubMedium> hasPubRhythmId(Long pubRhythmId) {
        return rhythmExists((cb, rhythm) -> cb.equal(rhythm.get("id"), pubRhythmId));
    }

    public static Specification<PubMedium> hasPubRhythm(String pubRhythm) {
        String pattern = containsPattern(pubRhythm);
        return rhythmExists((cb, rhythm) -> like(cb, rhythm.get("value"), pattern));
    }

    public static Specification<PubMedium> hasStartYear(Integer startYear) {
        return (root, query, cb) -> cb.equal(root.get("startYear"), Year.of(startYear));
    }

    public static Specification<PubMedium> hasEndYear(Integer endYear) {
        return (root, query, cb) -> cb.equal(root.get("endYear"), Year.of(endYear));
    }

    public static Specification<PubMedium> hasAmountVolumes(Long amountVolumes) {
        return (root, query, cb) -> cb.equal(root.get("amountVolumes"), amountVolumes);
    }

    public static Specification<PubMedium> hasAmountIssues(Long amountIssues) {
        return (root, query, cb) -> cb.equal(root.get("amountIssues"), amountIssues);
    }

    private static Specification<PubMedium> containsText(String attribute, String value) {
        String pattern = containsPattern(value);
        return (root, query, cb) -> like(cb, root.get(attribute), pattern);
    }

    /** EXISTS subquery over the publication places, so a medium with several places is not duplicated. */
    private static Specification<PubMedium> placeExists(BiFunction<CriteriaBuilder, Join<PubMedium, Place>, Predicate> condition) {
        return (root, query, cb) -> {
            Subquery<Integer> sub = query.subquery(Integer.class);
            Root<PubMedium> correlated = sub.correlate(root);
            Join<PubMedium, Place> place = correlated.join("publicationPlaces");
            sub.select(cb.literal(1)).where(condition.apply(cb, place));
            return cb.exists(sub);
        };
    }

    /** EXISTS subquery over the publication rhythms, so a medium with several rhythms is not duplicated. */
    private static Specification<PubMedium> rhythmExists(BiFunction<CriteriaBuilder, Join<PubMedium, PublicationRhythm>, Predicate> condition) {
        return (root, query, cb) -> {
            Subquery<Integer> sub = query.subquery(Integer.class);
            Root<PubMedium> correlated = sub.correlate(root);
            Join<PubMedium, PublicationRhythm> rhythm = correlated.join("pubRhytms");
            sub.select(cb.literal(1)).where(condition.apply(cb, rhythm));
            return cb.exists(sub);
        };
    }

    private static Predicate like(CriteriaBuilder cb, Expression<String> value, String pattern) {
        return cb.like(cb.lower(cb.coalesce(value, "")), pattern, '\\');
    }

    private static String containsPattern(String value) {
        String escaped = value.trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
