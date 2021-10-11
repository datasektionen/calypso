package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.entities.ReceptionMode;
import se.datasektionen.calypso.models.enums.ItemType;

import java.util.List;
import java.util.Optional;

public interface ReceptionRepository extends CrudRepository<ReceptionMode, Long> {

    @Query("select r from ReceptionMode r order by r.id desc")
    ReceptionMode get();

}
