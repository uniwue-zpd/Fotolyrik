package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import jakarta.persistence.criteria.CriteriaBuilder;
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
            Expression<String> firstName = cb.coalesce(root.<String>get("firstName"), "");
            Expression<String> lastName = cb.coalesce(root.<String>get("lastName"), "");
            Expression<String> fullName = cb.trim(cb.concat(cb.concat(firstName, " "), lastName));

            Subquery<Integer> pseudonymMatch = query.subquery(Integer.class);
            Root<Person> correlated = pseudonymMatch.correlate(root);
            Join<Person, String> pseudonym = correlated.join("pseudonyms");
            pseudonymMatch.select(cb.literal(1)).where(like(cb, pseudonym, pattern));

            return cb.or(
                    like(cb, fullName, pattern),
                    like(cb, firstName, pattern),
                    like(cb, lastName, pattern),
                    like(cb, root.get("studioName"), pattern),
                    cb.exists(pseudonymMatch)
            );
        };
    }

    public static Specification<Person> hasBirthYear(Integer birthYear) {
        return (root, query, cb) -> cb.equal(root.get("birthYear"), birthYear);
    }

    public static Specification<Person> hasDeathYear(Integer deathYear) {
        return (root, query, cb) -> cb.equal(root.get("deathYear"), deathYear);
    }

    /**
     * Matches persons with the given sex value ("männlich" or "weiblich").
     */
    public static Specification<Person> hasSex(String sex) {
        return (root, query, cb) -> cb.equal(root.get("sex"), sex);
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
