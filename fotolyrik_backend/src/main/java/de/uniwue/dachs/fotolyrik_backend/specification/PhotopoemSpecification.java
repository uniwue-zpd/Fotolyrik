package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

    public static Specification<Photopoem> hasSeries(String series) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("series")), "%" + series.toLowerCase() + "%");
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

    public static Specification<Photopoem> hasPubPlaceId(Long pubPlaceId) {
        return (root, query, criteriaBuilder) -> {
            if (pubPlaceId == null) {
                return null;
            }

            Join<Photopoem, PubMedium> pubMediumJoin = root.join("publicationMedium");
            Join<PubMedium, Place> placeJoin = pubMediumJoin.join("publicationPlaces");
            return criteriaBuilder.equal(placeJoin.get("id"), pubPlaceId);
        };
    }

    public static Specification<Photopoem> hasLocationId(Long locationId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("foundIn").get("id"), locationId);
    }

    private static Specification<Photopoem> hasRoleId(Long id, ContributionRole role){
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.distinct(true);

            Join<Photopoem, Contribution> contributions = root.join("contributions", JoinType.LEFT);
            Join<Contribution, Person> contributor = contributions.join("contributor", JoinType.LEFT);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(contributor.get("id"), id),
                    criteriaBuilder.equal(contributions.get("role"), role)
            );
        };
    }

    private static Specification<Photopoem> hasRole(String name, ContributionRole role){
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.distinct(true);

            String pattern = "%" + name.toLowerCase() + "%";

            Join<Photopoem, Contribution> contributions = root.join("contributions", JoinType.LEFT);
            Join<Contribution, Person> contributor = contributions.join("contributor", JoinType.LEFT);
            Join<Person, String> contributorPseudonyms = contributor.join("pseudonyms", JoinType.LEFT);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(contributions.get("role"), role),
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(contributor.get("firstName")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(contributor.get("lastName")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(contributorPseudonyms), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(contributions.get("pseudonym")), pattern)
                    )
            );
        };
    }

    public static Specification<Photopoem> hasAuthorId(Long authorId) {
        return hasRoleId(authorId, ContributionRole.AUTHOR);
    }

    public static Specification<Photopoem> hasAuthor(String authorName) {
        return hasRole(authorName, ContributionRole.AUTHOR);
    }

    public static Specification<Photopoem> hasPhotographerId(Long photographerId) {
        return hasRoleId(photographerId, ContributionRole.PHOTOGRAPHER);
    }

    public static Specification<Photopoem> hasPhotographer(String photographerName) {
        return hasRole(photographerName, ContributionRole.PHOTOGRAPHER);
    }

    public static Specification<Photopoem> hasDepictedPersonId(Long depictedPersonId) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> depictedPeople = root.join("depictedPeople");
            return criteriaBuilder.equal(depictedPeople.get("id"), depictedPersonId);
        };
    }

    public static Specification<Photopoem> hasDepictedPerson(String depictedPersonName) {
        return (root, query, criteriaBuilder) -> {
            Join<Photopoem, Person> depictedPeople = root.join("depictedPeople");
            Join<Person, String> pseudonyms = depictedPeople.join("pseudonyms", JoinType.LEFT);
            String pattern = "%" + depictedPersonName.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(depictedPeople.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(depictedPeople.get("lastName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(pseudonyms), pattern)
            );
        };
    }

    public static Specification<Photopoem> hasParticipantId(Long participantId) {
        return hasRoleId(participantId, ContributionRole.PARTICIPANT);
    }

    public static Specification<Photopoem> hasParticipant(String participantName) {
        return hasRole(participantName, ContributionRole.PARTICIPANT);
    }

    public static Specification<Photopoem> hasOtherContributorId(Long contributorId) {
        return hasRoleId(contributorId, ContributionRole.OTHER);
    }

    public static Specification<Photopoem> hasOtherContributor(String contributorName) {
        return hasRole(contributorName, ContributionRole.OTHER);
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

    public static Specification<Photopoem> hasCopyrightStatusImageId(Long copyrightStatusImageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("copyrightStatusImage").get("id"), copyrightStatusImageId);
    }

    public static Specification<Photopoem> hasCopyrightStatusImage(String copyrightStatusImage) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("copyrightStatusImage").get("value")), "%" + copyrightStatusImage.toLowerCase() + "%");
    }

    public static Specification<Photopoem> hasCopyrightStatusTextId(Long copyrightStatusTextId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("copyrightStatusText").get("id"), copyrightStatusTextId);
    }

    public static Specification<Photopoem> hasCopyrightStatusText(String copyrightStatusText) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("copyrightStatusText").get("value")), "%" + copyrightStatusText.toLowerCase() + "%");
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
