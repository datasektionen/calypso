package se.datasektionen.calypso.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import se.datasektionen.calypso.models.entities.ActivityPeriod;

import java.util.List;
import java.util.Optional;

public interface ActivityPeriodRepository extends CrudRepository<ActivityPeriod, Long> {

    @Override
    @PostAuthorize("hasPermission(returnObject, null)")
    Optional<ActivityPeriod> findById(Long id);

    @PostFilter("hasPermission(filterObject, null)")
    List<ActivityPeriod> findAllByActivityIdOrderByIdDesc(Long activityId);

    @Override
    @PreAuthorize("hasPermission(#period, null)")
    <S extends ActivityPeriod> S save(@Param("period") S period);

}
