package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;

import java.util.List;
import java.util.Optional;

public interface ApiRepository extends CrudRepository<Item, Long> {

	@Query("select e from Item e where e.itemType = 'EVENT' and e.publishDate <= CURRENT_TIMESTAMP and " +
			"e.eventStartTime > CURRENT_TIMESTAMP order by e.eventStartTime asc")
	List<Item> upcomingEvents();

	@Query("select e from Item e where e.itemType = 'EVENT' and e.publishDate <= CURRENT_TIMESTAMP")
	List<Item> allEvents();

	@Query("select i from Item i where i.itemType = ?1 and i.publishDate <= CURRENT_TIMESTAMP")
	Page<Item> findAllPublishedByItemType(ItemType itemType, Pageable pageable);

	@Query("select i from Item i where i.publishDate <= CURRENT_TIMESTAMP")
	Page<Item> findAllPublished(Pageable pageable);

	@Override
	@Query("select i from Item i where i.publishDate <= CURRENT_TIMESTAMP and i.id = ?1")
	Optional<Item> findById(Long id);

}
