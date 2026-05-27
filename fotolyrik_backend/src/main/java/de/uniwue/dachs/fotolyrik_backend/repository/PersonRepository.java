package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.KeywordCountDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("""
    SELECT
        k.value AS keyword,
        k.gndId AS gndId,
        k.id AS id,
        COUNT(DISTINCT p.id) AS count
    FROM Contribution c
    JOIN c.workContributedTo p
    JOIN p.themes k
    WHERE c.contributor.id = :personId
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
    WHERE c.contributor.id = :personId
    GROUP BY k.id, k.value
    ORDER BY COUNT(DISTINCT p.id) DESC
    LIMIT :limit
    """)
    List<KeywordCountDTO> findTopImageMotifsByPerson(Long personId, Long limit);
}
