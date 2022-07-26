package se.datasektionen.calypso.controllers.admin;

import lombok.RequiredArgsConstructor;
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
import se.datasektionen.calypso.models.repositories.ReceptionRepository;

import java.time.format.DateTimeFormatter;

@Controller
@PreAuthorize("hasAuthority('post')")
@RequiredArgsConstructor
public class ListController {

	private static final int PAGE_SIZE = 50;

	private final ItemRepository itemRepository;
	private final DateTimeFormatter formatter;
	private final ReceptionRepository receptionRepository;

	@RequestMapping("/admin/list")
	public String index(@RequestParam(name = "itemType", required = false) String itemType,
	                    @RequestParam(name = "sortBy", defaultValue = "publishDate") String sortBy,
	                    @RequestParam(name = "sort", defaultValue = "DESC") String sort,
	                    @RequestParam(name = "page", defaultValue = "0") int page,
											@RequestParam(name = "onlyMe", defaultValue = "false") boolean onlyMe,
	                    Authentication auth, Model model) {
		// Common objects
		var type = ItemType.valueOfIgnoreCase(itemType);
		var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.valueOf(sort), sortBy));
		var user = (DAuthUserDetails) auth.getPrincipal();
		var editor = user.getAuthorities().contains(new SimpleGrantedAuthority("editor"));
		Page<Item> items;


		// Items
		if (type == null)
			if (!onlyMe && editor)
				items = itemRepository.findAll(pageable);
			else
				items = itemRepository.findAllByAuthor(user.getUser(), pageable);
		else
			if (!onlyMe && editor)
				items = itemRepository.findAllByItemType(type, pageable);
			else
				items = itemRepository.findAllByItemTypeAndAuthor(type, user.getUser(), pageable);

		boolean receptionMode = receptionRepository.get() == null
			? false // reception mode not initialized in db, return false
			: receptionRepository.get().getState(); // get the state

		// Populate model
		model.addAttribute("formatter", formatter);
		model.addAttribute("page", page);
		model.addAttribute("items", items);
		model.addAttribute("itemType", type);
		model.addAttribute("onlyMe", onlyMe);
		model.addAttribute("receptionMode", receptionMode);
		return "list";
	}

}
