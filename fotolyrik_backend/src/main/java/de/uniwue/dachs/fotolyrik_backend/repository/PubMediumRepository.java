package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PubMediumMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PubMediumRepository extends JpaRepository<PubMedium, Long>, JpaSpecificationExecutor<PubMedium> {

    @Query("""
    SELECT pm FROM PubMedium pm
    WHERE LOWER(pm.title) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(pm.subtitle) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<PubMedium> searchPubMedia(@Param("query") String query, Pageable pageable);

    @Query(value = """
    SELECT
    COUNT(DISTINCT p.id) AS photopoems,
    COUNT(DISTINCT k.keyword_id) AS keywords,
    COUNT(DISTINCT phtr.contributor_id) AS photographers,
    COUNT(DISTINCT authr.contributor_id) AS authors,
    COUNT(DISTINCT dp.person_id) AS depictedPeople
    FROM pub_medium pm
    JOIN photopoem p ON p.pub_medium_id = pm.id
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
    LEFT JOIN photopoem_depicted_people dp
    ON dp.photopoem_id = p.id
    WHERE pm.id = :pubMediumId;
    """ ,nativeQuery=true)
    PubMediumMetricsDTO getMetricsByPubMedium(@Param("pubMediumId") Long pubMediumId);

    @Query(value = """
    WITH sorted_pub_mediums AS (
        SELECT 
            id,
            ROW_NUMBER() OVER (ORDER BY title ASC, subtitle ASC, id ASC) AS rn
        FROM pub_medium
    )
    SELECT pm.id
    FROM sorted_pub_mediums target
    JOIN sorted_pub_mediums pm 
      ON pm.rn BETWEEN (target.rn - :padding) AND (target.rn + :padding)
    WHERE target.id = :id
    ORDER BY pm.rn ASC
    """, nativeQuery = true)
    List<Long> findNeighborIdsById(@Param("id") Long id, @Param("padding") int padding);
}

