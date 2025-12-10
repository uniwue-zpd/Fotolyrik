package de.uniwue.dachs.fotolyrik_backend.specification;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for filtering {@link PubMedium} entities based on various attributes.
 */
public class PubMediumSpecification {
    public static Specification<PubMedium> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<PubMedium> hasSubtitle(String subtitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("subtitle")), "%" + subtitle.toLowerCase() + "%");
    }

    public static Specification<PubMedium> hasPubPlaceId(Long pubPlaceId) {
        return (root, query, criteriaBuilder) -> {
            Join<PubMedium, Place> places = root.join("publicationPlaces");
            return criteriaBuilder.equal(places.get("id"), pubPlaceId);
        };
    }

    public static Specification<PubMedium> hasPubPlace(String pubPlace) {
        return (root, query, criteriaBuilder) -> {
            Join<PubMedium, Place> places = root.join("publicationPlaces");
            return criteriaBuilder.like(criteriaBuilder.lower(places.get("name")), "%" + pubPlace.toLowerCase() + "%");
        };
    }

    public static Specification<PubMedium> hasPublisherId(Long publisherId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publisher").get("id"), publisherId);
    }

    public static Specification<PubMedium> hasPublisher(String publisher) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher").get("name")), "%" + publisher.toLowerCase() + "%");
    }

    public static Specification<PubMedium> hasPubRhythmId(Long pubRhythmId) {
        return (root, query, criteriaBuilder) -> {
            Join<PubMedium, PublicationRhythm> rhythms = root.join("pubRhytms");
            return criteriaBuilder.equal(rhythms.get("id"), pubRhythmId);
        };
    }

    public static Specification<PubMedium> hasPubRhythm(String pubRhythm) {
        return (root, query, criteriaBuilder) -> {
            Join<PubMedium, PublicationRhythm> rhythms = root.join("pubRhytms");
            return criteriaBuilder.like(criteriaBuilder.lower(rhythms.get("value")), "%" + pubRhythm.toLowerCase() + "%");
        };
    }

    public static Specification<PubMedium> hasEditorialOffice(String editorialOffice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("editorialOffice")), "%" + editorialOffice.toLowerCase() + "%");
    }

    public static Specification<PubMedium> hasStartYear(Long startYear) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("startYear"), startYear);
    }

    public static Specification<PubMedium> hasEndYear(Long endYear) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("endYear"), endYear);
    }

    public static Specification<PubMedium> hasAmountVolumes(Long amountVolumes) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("amountVolumes"), amountVolumes);
    }

    public static Specification<PubMedium> hasAmountIssues(Long amountIssues) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("amountIssues"), amountIssues);
    }

    public static Specification<PubMedium> hasZdbId(String zdbId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("zdbId")), "%" + zdbId.toLowerCase() + "%");
    }
}
