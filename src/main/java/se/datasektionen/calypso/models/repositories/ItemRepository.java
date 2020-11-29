package se.datasektionen.calypso.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Long> {

	Page<Item> findAll(Pageable pageable);

	Page<Item> findAllByAuthor(String author, Pageable pageable);

	Page<Item> findAllByItemType(ItemType itemType, Pageable pageable);

	Page<Item> findAllByItemTypeAndAuthor(ItemType itemType, String author, Pageable pageable);

	@Override
	@PostAuthorize("hasPermission(returnObject, null)")
	Optional<Item> findById(Long id);

	@Override
	@PreAuthorize("hasPermission(#item, null)")
	<S extends Item> S save(@Param("item") S item);

}
