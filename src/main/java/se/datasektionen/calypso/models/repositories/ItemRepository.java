package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;

public interface ItemRepository extends CrudRepository<Item, Long> {

	Page<Item> findAllByItemType(ItemType itemType, Pageable pageable);

	Page<Item> findAllByItemTypeAndAuthor(ItemType itemType, String author, Pageable pageable);

	@Override
	@PostAuthorize("hasPermission(returnObject, null)")
	Item findOne(Long id);
}
