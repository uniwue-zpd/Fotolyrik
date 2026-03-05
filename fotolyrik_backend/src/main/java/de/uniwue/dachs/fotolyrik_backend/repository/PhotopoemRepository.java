package de.uniwue.dachs.fotolyrik_backend.repository;

import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotopoemRepository extends JpaRepository<Photopoem, Long>, JpaSpecificationExecutor<Photopoem> {
    List<Photopoem> findAllByAuthors_Id(Long author_id);

    List<Photopoem> findAllByPhotographers_Id(Long photographer_id);

    List <Photopoem> findAllByDepictedPeople_Id(Long depicted_person_id);

    // TODO this there might need tweaking / be extended, but currently is not used???
    // List<Photopoem> findAllByAuthors_IdAndPhotographers_id(Long author_id, Long photographer_id);
    //List<Photopoem> findAllByPeople_Ids(List<Long> people_ids);
}
