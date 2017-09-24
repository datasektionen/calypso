package se.datasektionen.calypso.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ItemRepository;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;

@Controller
public class EditController {

	private final ItemRepository itemRepository;

	@Autowired
	public EditController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@GetMapping("/admin/new")
	public String newForm(Model model) {
		model.addAttribute("item", new Item());
		return "edit";
	}

	@GetMapping("/admin/edit")
	public String editForm(@RequestParam(name = "id") Long id, Model model) {
		Item item = itemRepository.findOne(id);

		if (item == null)
			throw new ResourceNotFoundException();

		model.addAttribute("item", item);
		return "edit";
	}

	@PostMapping("/admin/edit")
	public String doEdit(@ModelAttribute Item item) {
		item = itemRepository.save(item);

		return "redirect:/admin/edit?saved=true&id=" + item.getId();
	}

}
