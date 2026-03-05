package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotopoemRepository extends JpaRepository<Photopoem, Long>, JpaSpecificationExecutor<Photopoem> {
    List<Photopoem> findAllByAuthors_Id(Long author_id);

    List<Photopoem> findAllByPhotographers_Id(Long photographer_id);

    List<Photopoem> findAllByAuthors_IdAndPhotographers_id(Long author_id, Long photographer_id);

    @Query("SELECT p.id FROM Photopoem p ORDER BY p.id")
    List<Long> findAllIds();
}
