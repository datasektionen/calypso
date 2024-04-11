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
import se.datasektionen.calypso.models.repositories.ReceptionRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

	private static final int PAGE_SIZE = 25;
	private final ApiRepository apiRepository;
	private final ReceptionRepository receptionRepository;

	@RequestMapping("/list")
	public Page<Item> items(@RequestParam(name = "itemType", required = false) String itemType,
							@RequestParam(name = "sortBy", defaultValue = "publishDate") String sortBy,
							@RequestParam(name = "sort", defaultValue = "DESC") String sort,
							@RequestParam(name = "page", defaultValue = "0") int page) {
		var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.valueOf(sort), sortBy));

		// Reception mode is on, filter sensitive events
		if (receptionRepository.get().getState()) {
			return itemType == null
					? apiRepository.findAllPublishedNonSensitive(pageable)
					: apiRepository.findAllPublishedByItemTypeNonSensitive(ItemType.valueOfIgnoreCase(itemType), pageable);
		}

		return itemType == null
				? apiRepository.findAllPublished(pageable)
				: apiRepository.findAllPublishedByItemType(ItemType.valueOfIgnoreCase(itemType), pageable);
	}

	@RequestMapping("/event")
	public Collection<Item> upcomingEvents() {
		if (receptionRepository.get().getState()) {
			List<Item> items = apiRepository.upcomingEvents()
				.stream()
				.filter(i -> !i.isSensitive())
				.collect(Collectors.toList());
			return items;
		}

		return apiRepository.upcomingEvents();
	}

	@RequestMapping("/item/{id}")
	public Item item(@PathVariable("id") long itemId) {
		Optional<Item> item = apiRepository.findById(itemId);
		if (item.isPresent()) {
			if (receptionRepository.get().getState()) { // Reception mode is on
				if (!item.get().isSensitive()) return item.get();
			} else return item.get();
		}
		throw new ResourceNotFoundException();
	}

}
