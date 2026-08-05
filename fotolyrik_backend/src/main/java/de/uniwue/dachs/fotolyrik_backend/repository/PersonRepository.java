package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.KeywordCountDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query(value = "SELECT DISTINCT pl.* " + // Use specific columns or pl.* depending on your needs
            "FROM contribution c " +
            "JOIN photopoem p         ON c.photopoem_id = p.id " +
            "JOIN pub_medium_place mp ON p.pub_medium_id = mp.pub_medium_id " +
            "JOIN place pl            ON mp.pub_place_id = pl.id " +
            "WHERE c.contributor_id = :personId",
            nativeQuery = true)
    List<Place> findContributionPlacesByPersonId(@Param("personId") Long personId);
    @Query("""
    SELECT
        k.value AS keyword,
        k.gndId AS gndId,
        k.id AS id,
        COUNT(DISTINCT p.id) AS count
    FROM Contribution c
    JOIN c.workContributedTo p
    JOIN p.themes k
    WHERE c.contributor.id = :personId AND c.role = 'AUTHOR'
    GROUP BY k.id, k.value
    ORDER BY COUNT(DISTINCT p.id) DESC
    LIMIT :limit
    """)
    List<KeywordCountDTO> findTopThemesByPerson(Long personId, Long limit);

    @Query("""
    SELECT
        k.value AS keyword,
        k.gndId AS gndId,
        k.id AS id,
        COUNT(DISTINCT p.id) AS count
    FROM Contribution c
    JOIN c.workContributedTo p
    JOIN p.imageMotifs k
    WHERE c.contributor.id = :personId AND c.role = 'AUTHOR'
    GROUP BY k.id, k.value
    ORDER BY COUNT(DISTINCT p.id) DESC
    LIMIT :limit
    """)
    List<KeywordCountDTO> findTopImageMotifsByPerson(Long personId, Long limit);

    @Query(value = """
    SELECT
        COUNT(DISTINCT p.id) AS photopoems,
        COUNT(DISTINCT pm.id) AS pubMedia,
        COUNT(DISTINCT k.keyword_id) AS keywords,
        COUNT(DISTINCT phtr.contributor_id) AS photographersWorkedWith,
        COUNT(DISTINCT authr.contributor_id) AS authorsWorkedWith
    FROM contribution c
    JOIN photopoem p ON c.photopoem_id = p.id
    LEFT JOIN pub_medium pm ON p.pub_medium_id = pm.id
    LEFT JOIN (
        SELECT pt.photopoem_id AS photopoem_id, pt.keyword_id AS keyword_id
        FROM photopoem_themes pt
        UNION
        SELECT pim.photopoem_id AS photopoem_id, pim.keyword_id AS keyword_id
        FROM photopoem_image_motifs pim
    ) k ON k.photopoem_id = p.id
    LEFT JOIN contribution phtr
        ON phtr.photopoem_id = p.id
        AND phtr.role = 'PHOTOGRAPHER'
    LEFT JOIN contribution authr
        ON authr.photopoem_id = p.id
        AND authr.role = 'AUTHOR'
    WHERE c.contributor_id = :personId
    """, nativeQuery = true)
    PersonMetricsDTO getMetricsByPerson(Long personId);

    @Query("""
    SELECT DISTINCT p FROM Person p
        LEFT JOIN p.pseudonyms pseudonym
        WHERE
        LOWER(CONCAT(COALESCE(p.firstName, ''), ' ', COALESCE(p.lastName, '')))
            LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(p.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(p.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(p.studioName, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(pseudonym, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY p.lastName ASC
    """)
    List<Person> searchPeople(@Param("query") String query);
}
