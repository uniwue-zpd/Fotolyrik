package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextSearchResult;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FullTextRepository extends JpaRepository<FullText, Long> {

/**
 * Full-text search for photopoems based on the query string.
 * @param query The search query string.
 * @return A list of FullTextSearchResult objects containing the ID of the photopoem, its title and highlighted search results.
 * **/
    @Query(value = """
SELECT
    ft.photopoem_id,
    p.title AS photopoem_title,
    ts_headline('german', ft.full_text, plainto_tsquery('german', :query)) AS query_result
FROM full_text ft
JOIN photopoem p ON ft.photopoem_id = p.id
WHERE ft.full_text_vector @@ plainto_tsquery('german', :query)""", nativeQuery = true)
    List<FullTextSearchResult> searchFullText(@Param("query") String query);

    Optional<FullText> findByPhotopoemId(Long photopoemId);

    Boolean existsByPhotopoemId(Long photopoemId);

    void deleteByPhotopoemId(Long photopoemId);
}
