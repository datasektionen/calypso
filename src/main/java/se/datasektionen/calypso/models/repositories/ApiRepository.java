package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;

import java.util.Collection;

public interface ApiRepository extends CrudRepository<Item, Long> {

	@Query("select e from Item e where e.itemType = 'EVENT' and e.publishDate <= CURRENT_TIMESTAMP and " +
			"e.eventStartTime > CURRENT_TIMESTAMP order by e.eventStartTime asc")
	Collection<Item> upcomingEvents();

	@Query("select e from Item e where e.itemType = 'EVENT' and e.publishDate <= CURRENT_TIMESTAMP")
	Collection<Item> allEvents();

	@Query("select i from Item i where i.itemType = ?1 and i.publishDate <= CURRENT_TIMESTAMP")
	Page<Item> findAllPublishedByItemType(ItemType itemType, Pageable pageable);

	@Query("select i from Item i where i.publishDate <= CURRENT_TIMESTAMP")
	Page<Item> findAllPublished(Pageable pageable);

}
