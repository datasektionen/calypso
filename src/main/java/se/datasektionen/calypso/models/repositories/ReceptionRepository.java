package se.datasektionen.calypso.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.datasektionen.calypso.models.entities.ReceptionMode;

public interface ReceptionRepository extends CrudRepository<ReceptionMode, Long> {

    @Query("select r from ReceptionMode r order by r.id desc")
    ReceptionMode get();

}
