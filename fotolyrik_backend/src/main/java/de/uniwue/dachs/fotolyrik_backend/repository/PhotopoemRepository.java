package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotopoemRepository extends JpaRepository<Photopoem, Long> {
    List<Photopoem> findAllByAuthors_Id(Long author_id);

    List<Photopoem> findAllByPhotographers_Id(Long photographer_id);

    List<Photopoem> findAllByAuthors_IdAndPhotographers_id(Long author_id, Long photographer_id);
}
