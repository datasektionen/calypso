package se.datasektionen.calypso.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.Activity;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ActivityRepository;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.Darkmode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
	private static final int MAX_TIME_SPAN_DAYS = 14;
	private final ApiRepository apiRepository;
	private final Darkmode darkmode;
	private final ActivityRepository activityRepository;

	@RequestMapping("/list")
	public Page<Item> items(@RequestParam(name = "itemType", required = false) String itemType,
							@RequestParam(name = "sortBy", defaultValue = "publishDate") String sortBy,
							@RequestParam(name = "sort", defaultValue = "DESC") String sort,
							@RequestParam(name = "page", defaultValue = "0") int page) {
		var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.valueOf(sort), sortBy));

		// Darkmode is on, filter sensitive events
		if (darkmode.getCurrent()) {
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
		if (darkmode.getCurrent()) {
			List<Item> items = apiRepository.upcomingEvents()
				.stream()
				.filter(i -> !i.isSensitive())
				.collect(Collectors.toList());
			return items;
		}

		return apiRepository.upcomingEvents();
	}

	@RequestMapping("/event/span")
	public Collection<Item> eventsInTimeSpan(
		@RequestParam("startDate") LocalDateTime startDate,
		@RequestParam("endDate") LocalDateTime endDate
	) {
		long days = startDate.until(endDate, ChronoUnit.DAYS);
		if (days > MAX_TIME_SPAN_DAYS) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Time span may not be larger than " + MAX_TIME_SPAN_DAYS + " days");
		}

		return apiRepository.eventsInTimeSpan(startDate, endDate, darkmode.getCurrent());
	}

	@RequestMapping("/item/{id}")
	public Item item(@PathVariable("id") long itemId) {
		Optional<Item> item = apiRepository.findById(itemId);
		if (item.isPresent()) {
			if (darkmode.getCurrent()) {
				if (!item.get().isSensitive()) return item.get();
			} else return item.get();
		}
		throw new ResourceNotFoundException();
	}

	@GetMapping("/activities")
	public Page<Activity> listActivities(@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "sort", defaultValue = "DESC") Direction sort,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sort, sortBy));

		// Reception mode is on, filter sensitive events
		if (darkmode.getCurrent()) {
			return activityRepository.findBySensitiveFalse(pageable);
		} else {
			return activityRepository.findAll(pageable);
		}
	}

}
