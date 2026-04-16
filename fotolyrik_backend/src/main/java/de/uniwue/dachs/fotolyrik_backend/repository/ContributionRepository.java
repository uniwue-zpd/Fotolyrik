package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {}
