package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelection;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotopoemSelectionRepository extends JpaRepository<PhotopoemSelection, Long> {
    List<PhotopoemSelection> findAllByTypeOrderByIdAsc(PhotopoemSelectionType type);

    boolean existsByPhotopoem_IdAndIdNot(Long photopoemId, Long id);

    boolean existsByPhotopoem_Id(Long photopoemId);

    @Query("SELECT s FROM PhotopoemSelection s WHERE s.id = :id")
    Optional<PhotopoemSelection> findByIdForUpdate(@Param("id") Long id);
}
