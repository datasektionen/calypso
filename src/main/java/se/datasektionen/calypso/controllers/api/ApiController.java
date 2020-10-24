package se.datasektionen.calypso.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ApiRepository;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

	private static final int PAGE_SIZE = 25;
	private final ApiRepository apiRepository;

	@RequestMapping("/list")
	public Page<Item> items(@RequestParam(name = "itemType", required = false) String itemType,
							@RequestParam(name = "sortBy", defaultValue = "publishDate") String sortBy,
							@RequestParam(name = "sort", defaultValue = "DESC") String sort,
							@RequestParam(name = "page", defaultValue = "0") int page) {
		var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.valueOf(sort), sortBy));

		return itemType == null
				? apiRepository.findAllPublished(pageable)
				: apiRepository.findAllPublishedByItemType(ItemType.valueOfIgnoreCase(itemType), pageable);
	}

	@RequestMapping("/event")
	public Collection<Item> upcomingEvents() {
		return apiRepository.upcomingEvents();
	}

	@RequestMapping("/item/{id}")
	public Item item(@PathVariable("id") long itemId) {
		return apiRepository.findById(itemId).orElseThrow(ResourceNotFoundException::new);
	}

}
