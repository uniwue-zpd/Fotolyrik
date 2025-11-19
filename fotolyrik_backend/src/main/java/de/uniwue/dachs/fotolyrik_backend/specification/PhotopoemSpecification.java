package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering {@link Photopoem} entities based on various attributes.
 */
public class PhotopoemSpecification {
    public static Specification<Photopoem> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasSubtitle(String subtitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("subtitle")), "%" + subtitle.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasAltTitle(String altTitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("altTitle")), "%" + altTitle.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasVolume(Long volume) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("volume"), volume);
    }

    public static Specification<Photopoem> hasIssue(Long issue) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("issue"), issue);
    }

    public static Specification<Photopoem> hasPublicationDate(String publicationDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("publicationDate")), "%" + publicationDate.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasPubMediumId(Long pubMediumId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publicationMedium").get("id"), pubMediumId);
    }

    public static Specification<Photopoem> hasPubMedium(String pubMedium) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("publicationMedium").get("title")), "%" + pubMedium.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> authors = root.join("authors");
            return criteriaBuilder.equal(authors.get("id"), authorId);
        };
    }

    public static Specification<Photopoem> hasAuthor(String authorName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> authors = root.join("authors");
            String pattern = "%" + authorName.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(authors.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(authors.get("lastName")), pattern)
            );
        };
    }

    public static Specification<Photopoem> hasPhotographerId(Long photographerId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> photographers = root.join("photographers");
            return criteriaBuilder.equal(photographers.get("id"), photographerId);
        };
    }

    public static Specification<Photopoem> hasPhotographer(String photographerName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> photographers = root.join("photographers");
            String pattern = "%" + photographerName.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(photographers.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(photographers.get("lastName")), pattern)
            );
        };
    }

    public static Specification<Photopoem> hasOtherContributorId(Long contributorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> otherContributors = root.join("otherContributors");
            return criteriaBuilder.equal(otherContributors.get("id"), contributorId);
        };
    }

    public static Specification<Photopoem> hasOtherContributor(String contributorName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> otherContributors = root.join("otherContributors");
            String pattern = "%" + contributorName.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(otherContributors.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(otherContributors.get("lastName")), pattern)
            );
        };
    }

    public static Specification<Photopoem> hasThemeId(Long themeId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Keyword> themes = root.join("themes");
            return criteriaBuilder.equal(themes.get("id"), themeId);
        };
    }

    public static Specification<Photopoem> hasTheme(String themeName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Keyword> themes = root.join("themes");
            return criteriaBuilder.like(criteriaBuilder.lower(themes.get("value")), "%" + themeName.toLowerCase() + "%");
        };
    }

    public static Specification<Photopoem> hasImageMotifId(Long motifId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Keyword> imageMotifs = root.join("imageMotifs");
            return criteriaBuilder.equal(imageMotifs.get("id"), motifId);
        };
    }

    public static Specification<Photopoem> hasImageMotif(String motifName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Keyword> imageMotifs = root.join("imageMotifs");
            return criteriaBuilder.like(criteriaBuilder.lower(imageMotifs.get("value")), "%" + motifName.toLowerCase() + "%");
        };
    }

    public static Specification<Photopoem> hasLanguageId(Long languageId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Language> languages = root.join("languages");
            return criteriaBuilder.equal(languages.get("id"), languageId);
        };
    }

    public static Specification<Photopoem> hasLanguage(String languageName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Language> languages = root.join("languages");
            String pattern = "%" + languageName.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(languages.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(languages.get("isoDesignation")), pattern)
            );
        };
    }
}
