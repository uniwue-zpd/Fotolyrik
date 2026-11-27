package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

/**
 * Specifications for filtering {@link Publisher} entities based on various attributes.
 */
public final class PublisherSpecification {
    private PublisherSpecification() {}

    /** Matches publishers whose name contains the given text (case-insensitive). */
    public static Specification<Publisher> hasName(String name) {
        String pattern = containsPattern(name);
        return (root, query, cb) -> like(cb, root.get("name"), pattern);
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
