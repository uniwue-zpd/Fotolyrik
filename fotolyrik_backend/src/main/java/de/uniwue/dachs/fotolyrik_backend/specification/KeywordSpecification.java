package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

/**
 * Specifications for filtering {@link Keyword} entities based on various attributes.
 */
public final class KeywordSpecification {
    private KeywordSpecification() {}

    /** Matches keywords whose value contains the given text (case-insensitive). */
    public static Specification<Keyword> hasValue(String value) {
        String pattern = containsPattern(value);
        return (root, query, cb) -> like(cb, root.get("value"), pattern);
    }

    /** Matches keywords with exactly the given GND identifier. */
    public static Specification<Keyword> hasGndId(String gndId) {
        return (root, query, cb) -> cb.like(root.get("gndId"), gndId.trim());
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
