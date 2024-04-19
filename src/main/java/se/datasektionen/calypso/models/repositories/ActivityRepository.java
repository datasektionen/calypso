package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import se.datasektionen.calypso.models.entities.Activity;

import java.util.Optional;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    Page<Activity> findAll(Pageable pageable);

    Page<Activity> findAllByAuthor(String author, Pageable pageable);

    Page<Activity> findBySensitiveFalse(Pageable pageable);

    @Override
    @PostAuthorize("hasPermission(returnObject, null)")
    Optional<Activity> findById(Long id);

    @Override
    @PreAuthorize("hasPermission(#activity, null)")
    <S extends Activity> S save(@Param("activity") S activity);

}
