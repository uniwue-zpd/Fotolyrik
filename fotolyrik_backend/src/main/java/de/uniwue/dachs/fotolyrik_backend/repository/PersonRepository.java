package de.uniwue.dachs.fotolyrik_backend.repository;

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
}
