package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRhythmRepository extends JpaRepository<PublicationRhythm, Long> {}
