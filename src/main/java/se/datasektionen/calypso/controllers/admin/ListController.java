package se.datasektionen.calypso.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ItemRepository;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class ListController {

	private final ItemRepository itemRepository;
	private static final int PAGE_SIZE = 50;

	@Autowired
	public ListController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping("/admin/list")
	public String index(@RequestParam(name = "itemType", defaultValue = "post") String itemType,
	                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	                        @RequestParam(name = "sort", defaultValue = "DESC") String sort,
	                        @RequestParam(name = "page", defaultValue = "0") int page,
	                        Model model) {
		model.addAttribute("formatter", DateTimeFormatter.ofPattern("d MMM YYYY HH:mm", Locale.forLanguageTag("sv")));
		model.addAttribute("page", page);
		model.addAttribute("items",
				itemRepository.findAllByItemType(
						ItemType.valueOfIgnoreCase(itemType),
						new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.valueOf(sort), sortBy))));

		return "list";
	}

}
