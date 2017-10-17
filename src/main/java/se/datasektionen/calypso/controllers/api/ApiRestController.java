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
import se.datasektionen.calypso.models.repositories.ApiRepository;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class ApiRestController {

	private static final int PAGE_SIZE = 25;
	private ApiRepository apiRepository;

	@Autowired
	public ApiRestController(ApiRepository apiRepository) {
		this.apiRepository = apiRepository;
	}

	@RequestMapping("/list")
	public Page<Item> items(@RequestParam(name = "itemType", required = false) String itemType,
	                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	                        @RequestParam(name = "sort", defaultValue = "DESC") String sort,
	                        @RequestParam(name = "page", defaultValue = "0") int page) {
		PageRequest pageable = new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.valueOf(sort), sortBy));

		if (itemType == null)
			return apiRepository.findAllPublished(pageable);
		else
			return apiRepository.findAllPublishedByItemType(ItemType.valueOfIgnoreCase(itemType), pageable);
	}

	@RequestMapping("/event")
	public Collection<Item> upcomingEvents() {
		return apiRepository.upcomingEvents();
	}

}
