package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

/**
 * Specifications for filtering {@link Person} entities based on various attributes.
 */
public final class PersonSpecification {
    private PersonSpecification() {}

    /**
     * Matches persons whose full name, first name, last name, studio name or any pseudonym
     * contains the given value (case-insensitive).
     */
    public static Specification<Person> hasName(String name) {
        String pattern = containsPattern(name);
        return (root, query, cb) -> {
            Expression<String> firstName = cb.coalesce(root.get("firstName"), "");
            Expression<String> lastName = cb.coalesce(root.get("lastName"), "");
            Expression<String> fullName = cb.trim(cb.concat(cb.concat(firstName, " "), lastName));

            return cb.or(
                    like(cb, fullName, pattern),
                    like(cb, firstName, pattern),
                    like(cb, lastName, pattern),
                    like(cb, root.get("studioName"), pattern),
                    pseudonymExists(root, query, cb, pattern)
            );
        };
    }

    /** Matches persons whose studio name contains the given value (case-insensitive). */
    public static Specification<Person> hasStudioName(String studioName) {
        String pattern = containsPattern(studioName);
        return (root, query, cb) -> like(cb, root.get("studioName"), pattern);
    }

    /** Matches persons having at least one pseudonym that contains the given value (case-insensitive). */
    public static Specification<Person> hasPseudonym(String pseudonym) {
        String pattern = containsPattern(pseudonym);
        return (root, query, cb) -> pseudonymExists(root, query, cb, pattern);
    }

    /** Matches persons with exactly the given GND identifier. */
    public static Specification<Person> hasGndId(String gndId) {
        return (root, query, cb) -> cb.equal(root.get("gndId"), gndId.trim());
    }

    public static Specification<Person> hasBirthYear(Integer birthYear) {
        return (root, query, cb) -> cb.equal(root.get("birthYear"), birthYear);
    }

    public static Specification<Person> hasDeathYear(Integer deathYear) {
        return (root, query, cb) -> cb.equal(root.get("deathYear"), deathYear);
    }

    /**
     * Matches persons whose birth year lies within the given inclusive range. Either bound may be {@code null}.
     */
    public static Specification<Person> birthYearBetween(Integer from, Integer to) {
        return yearBetween("birthYear", from, to);
    }

    /**
     * Matches persons whose death year lies within the given inclusive range. Either bound may be {@code null}.
     */
    public static Specification<Person> deathYearBetween(Integer from, Integer to) {
        return yearBetween("deathYear", from, to);
    }

    /**
     * Matches persons with the given sex value ("männlich" or "weiblich").
     */
    public static Specification<Person> hasSex(String sex) {
        return (root, query, cb) -> cb.equal(root.get("sex"), sex);
    }

    private static Specification<Person> yearBetween(String attribute, Integer from, Integer to) {
        return (root, query, cb) -> {
            Expression<Integer> year = root.get(attribute);
            if (from != null && to != null) return cb.between(year, from, to);
            if (from != null) return cb.greaterThanOrEqualTo(year, from);
            if (to != null) return cb.lessThanOrEqualTo(year, to);
            return cb.conjunction();
        };
    }

    private static Predicate pseudonymExists(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb, String pattern) {
        Subquery<Integer> pseudonymMatch = query.subquery(Integer.class);
        Root<Person> correlated = pseudonymMatch.correlate(root);
        Join<Person, String> pseudonym = correlated.join("pseudonyms");
        pseudonymMatch.select(cb.literal(1)).where(like(cb, pseudonym, pattern));
        return cb.exists(pseudonymMatch);
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
