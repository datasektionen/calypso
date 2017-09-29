package se.datasektionen.calypso.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ItemRepository;

@RestController
@RequestMapping("/api")
public class APIController {

	private final ItemRepository itemRepository;
	private static final int PAGE_SIZE = 50;

	@Autowired
	public APIController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping("/list")
	public Page<Item> items(@RequestParam(name = "itemType", defaultValue = "post") String itemType,
	                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	                        @RequestParam(name = "sort", defaultValue = "DESC") String sort,
	                        @RequestParam(name = "page", defaultValue = "0") int page) {
		return itemRepository.findAllByItemType(
				ItemType.valueOfIgnoreCase(itemType),
				new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.valueOf(sort), sortBy)));
	}

}
