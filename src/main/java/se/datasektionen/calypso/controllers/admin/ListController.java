package se.datasektionen.calypso.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ItemRepository;

import java.time.format.DateTimeFormatter;

@Controller
@PreAuthorize("hasAuthority('post')")
public class ListController {

	private final ItemRepository itemRepository;
	private final DateTimeFormatter formatter;
	private static final int PAGE_SIZE = 50;

	@Autowired
	public ListController(ItemRepository itemRepository, DateTimeFormatter formatter) {
		this.itemRepository = itemRepository;
		this.formatter = formatter;
	}

	@RequestMapping("/admin/list")
	public String index(@RequestParam(name = "itemType", defaultValue = "post") String itemType,
	                    @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	                    @RequestParam(name = "sort", defaultValue = "DESC") String sort,
	                    @RequestParam(name = "page", defaultValue = "0") int page,
	                    Authentication auth, Model model) {
		// Common objects
		ItemType type = ItemType.valueOfIgnoreCase(itemType);
		PageRequest pageable = new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.valueOf(sort), sortBy));
		DAuthUserDetails user = (DAuthUserDetails) auth.getPrincipal();
		Page<Item> items;

		// Items
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("editor")))
			items = itemRepository.findAllByItemType(type, pageable);
		else
			items = itemRepository.findAllByItemTypeAndAuthor(type, user.getUser(), pageable);

		// Populate model
		model.addAttribute("formatter", formatter);
		model.addAttribute("page", page);
		model.addAttribute("items", items);
		model.addAttribute("itemType", type);
		return "list";
	}

}
