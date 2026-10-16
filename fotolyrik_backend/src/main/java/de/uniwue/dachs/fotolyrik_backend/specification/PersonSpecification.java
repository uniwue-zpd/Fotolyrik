package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.PersonSearchField;
import de.uniwue.dachs.fotolyrik_backend.model.PersonSearchMatchMode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PersonSpecification {
    private PersonSpecification() {}

    public static Specification<Person> search(String query, PersonSearchField field,
                                               PersonSearchMatchMode matchMode) {
        String normalizedQuery = query.toLowerCase(Locale.ROOT);
        PersonSearchMatchMode positiveMode = matchMode.positiveMode();

        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> fieldMatches = new ArrayList<>();

            if (field == PersonSearchField.ALL || field == PersonSearchField.FULL_NAME) {
                Expression<String> fullName = criteriaBuilder.trim(criteriaBuilder.concat(
                        criteriaBuilder.concat(
                                criteriaBuilder.coalesce(root.<String>get("firstName"), ""),
                                " "
                        ),
                        criteriaBuilder.coalesce(root.<String>get("lastName"), "")
                ));
                fieldMatches.add(textMatch(criteriaBuilder, fullName, positiveMode, normalizedQuery));
            }
            if (field == PersonSearchField.ALL || field == PersonSearchField.BIRTH_YEAR) {
                Expression<String> birthYear = criteriaBuilder.coalesce(
                        criteriaBuilder.function("concat", String.class,
                                root.<Integer>get("birthYear"), criteriaBuilder.literal("")), "");
                fieldMatches.add(textMatch(criteriaBuilder, birthYear, positiveMode, normalizedQuery));
            }
            if (field == PersonSearchField.ALL || field == PersonSearchField.DEATH_YEAR) {
                Expression<String> deathYear = criteriaBuilder.coalesce(
                        criteriaBuilder.function("concat", String.class,
                                root.<Integer>get("deathYear"), criteriaBuilder.literal("")), "");
                fieldMatches.add(textMatch(criteriaBuilder, deathYear, positiveMode, normalizedQuery));
            }
            if (field == PersonSearchField.ALL || field == PersonSearchField.PSEUDONYMS) {
                fieldMatches.add(pseudonymMatch(root, criteriaQuery, criteriaBuilder,
                        positiveMode, normalizedQuery));
            }

            Predicate anyFieldMatches = criteriaBuilder.or(fieldMatches.toArray(Predicate[]::new));
            return matchMode.isNegative() ? criteriaBuilder.not(anyFieldMatches) : anyFieldMatches;
        };
    }

    private static Predicate textMatch(CriteriaBuilder criteriaBuilder, Expression<String> value,
                                       PersonSearchMatchMode matchMode, String query) {
        Expression<String> normalizedValue = criteriaBuilder.lower(criteriaBuilder.coalesce(value, ""));
        String escapedQuery = query.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");

        return switch (matchMode) {
            case STARTS_WITH -> criteriaBuilder.like(normalizedValue, escapedQuery + "%", '\\');
            case CONTAINS -> criteriaBuilder.like(normalizedValue, "%" + escapedQuery + "%", '\\');
            case ENDS_WITH -> criteriaBuilder.like(normalizedValue, "%" + escapedQuery, '\\');
            case EQUALS -> criteriaBuilder.equal(normalizedValue, query);
            case NOT_CONTAINS, NOT_EQUALS -> throw new IllegalArgumentException(
                    "A positive match mode is required");
        };
    }

    private static Predicate pseudonymMatch(Root<Person> root,
                                           jakarta.persistence.criteria.CriteriaQuery<?> criteriaQuery,
                                           CriteriaBuilder criteriaBuilder,
                                           PersonSearchMatchMode matchMode, String query) {
        Subquery<Integer> matchingPseudonym = criteriaQuery.subquery(Integer.class);
        Root<Person> correlatedPerson = matchingPseudonym.correlate(root);
        Join<Person, String> pseudonym = correlatedPerson.join("pseudonyms");
        matchingPseudonym.select(criteriaBuilder.literal(1))
                .where(textMatch(criteriaBuilder, pseudonym, matchMode, query));
        return criteriaBuilder.exists(matchingPseudonym);
    }
}
