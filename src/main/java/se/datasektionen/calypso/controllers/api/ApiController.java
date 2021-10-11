package se.datasektionen.calypso.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.stream.Stream;

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

		apiRepository.findAll().forEach(i -> {
			if (!i.getTest()) i.setTest(false);
			apiRepository.save(i);
		});

		// Reception mode is on, filter sensitive events
		if (receptionRepository.get().getState()) {
			// https://stackoverflow.com/a/57252328/16911837
			List<Item> s1 = apiRepository.findAllPublished(pageable)
				.stream()
				.filter(i -> !i.isSensitive())
				.collect(Collectors.toList());
			List<Item> s2 = apiRepository.findAllPublishedByItemType(ItemType.valueOfIgnoreCase(itemType), pageable)
				.stream()
				.filter(i -> !i.isSensitive())
				.collect(Collectors.toList());
			return itemType == null ? new PageImpl<Item>(s1) : new PageImpl<Item>(s2);
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
		if (item.isPresent() && !receptionRepository.get().getState()) return item.get();
		else throw new ResourceNotFoundException();
	}

}
