package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PubMediumMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PubMediumRepository extends JpaRepository<PubMedium, Long>, JpaSpecificationExecutor<PubMedium> {

    PubMediumMetricsDTO getMetricsByPubMedium(Long personId);
}

