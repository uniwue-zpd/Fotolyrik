package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotopoemRepository extends JpaRepository<Photopoem, Long> {}
