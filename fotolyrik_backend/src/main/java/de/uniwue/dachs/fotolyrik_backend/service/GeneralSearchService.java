package de.uniwue.dachs.fotolyrik_backend.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.EntityManager;

import de.uniwue.dachs.fotolyrik_backend.DTO.GeneralSearchDTO;


@Service
public class GeneralSearchService {

    @PersistenceContext
    private EntityManager em;

    public List<GeneralSearchDTO> search(String query) {
        String pattern = "%" + query.toLowerCase() + "%";

        return Stream.of(
                new SearchSpec("SELECT id, CONCAT(first_name, ' ', last_name) AS title FROM person WHERE LOWER(CONCAT(first_name, ' ', last_name)) LIKE :pattern", "person"),
                new SearchSpec("SELECT p.id, CONCAT(p.first_name, ' ', p.last_name) AS title FROM person p JOIN person_pseudonymes pp ON p.id = pp.person_id WHERE LOWER(pp.pseudonyms) LIKE :pattern", "person"),
                new SearchSpec("SELECT id, title FROM photopoem WHERE LOWER(title) LIKE :pattern", "photopoem"),
                new SearchSpec("SELECT id, name AS title FROM place WHERE LOWER(name) LIKE :pattern", "place"),
                new SearchSpec("SELECT id, CONCAT(title, ' ', subtitle) FROM pub_medium WHERE LOWER(CONCAT(title, ' ', subtitle)) LIKE :pattern", "pub_medium")
            )
            .flatMap(spec -> searchDatabase(spec.sql, pattern, spec.type).stream())
            .toList();
    }

    private List<GeneralSearchDTO> searchDatabase(String sql, String pattern, String type) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = (List<Object[]>) em.createNativeQuery(sql)
            .setParameter("pattern", pattern)
            .getResultList();

        return rows.stream()
            .map(row -> new GeneralSearchDTO(((Number) row[0]).longValue(), ((String) row[1]).strip(), type))
            .toList();
    }

    private static class SearchSpec {
        final String sql;
        final String type;
        SearchSpec(String sql, String type) {
            this.sql = sql;
            this.type = type;
        }
    }
}
