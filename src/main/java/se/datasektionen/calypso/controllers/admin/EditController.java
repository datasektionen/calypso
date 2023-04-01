package se.datasektionen.calypso.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ItemRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@PreAuthorize("hasAuthority('post')")
@RequiredArgsConstructor
public class EditController {

	private final ItemRepository itemRepository;
	private final DateTimeFormatter formatter;

	@GetMapping("/admin/new")
	public String newForm(Authentication auth, Model model) {
		var user = (DAuthUserDetails) auth.getPrincipal();

		var item = new Item();
		item.setAuthor(user.getUser());
		item.setAuthorDisplay(user.getName());

		model.addAttribute("item", item);
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		return "edit";
	}

	@GetMapping("/admin/duplicate")
	public String duplicateForm(@RequestParam(name = "id") Long id, Authentication auth, Model model) {
		var user = (DAuthUserDetails) auth.getPrincipal();
		var baseItem = itemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

		if (baseItem == null)
			throw new ResourceNotFoundException();

    var item = baseItem.duplicate();

		item.setAuthor(user.getUser());
		item.setAuthorDisplay(user.getName());
		model.addAttribute("item", item);
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		return "edit";
	}

	@GetMapping("/admin/edit")
	public String editForm(@RequestParam(name = "id") Long id, Model model) {
		var item = itemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

		if (item == null)
			throw new ResourceNotFoundException();

		model.addAttribute("item", item);
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		return "edit";
	}

	@PostMapping("/admin/edit")
	public String doEdit(Authentication auth, @RequestParam(required = false) String publish, @Valid Item item, BindingResult bindingResult, Model model) {
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		// Check for form errors
		if (bindingResult.hasErrors())
			return "edit";

		var originalItem = itemRepository.findById(item.getId()).get();

		var user = (DAuthUserDetails) auth.getPrincipal();
		if (originalItem != null && item.getAuthor() != user.getUser() ){
			item.setPublishAs(originalItem.getPublishAs());
			item.setPublishAsDisplay(originalItem.getPublishAsDisplay());
		}
		else if (item.getPublishAs() != null && !item.getPublishAs().isEmpty()) {
			var mandates = user.getMandates();
			String mandateDisplay = mandates.get(item.getPublishAs());
			if (mandateDisplay != null)
				item.setPublishAsDisplay(mandateDisplay);
		}

		// If the special publish param (name of a submit button) is present, we publish
		if (publish != null)
			item.setPublishDate(LocalDateTime.now());

		item = itemRepository.save(item);

		return "redirect:/admin/edit?saved=true&id=" + item.getId();
	}

	@PreAuthorize("hasAuthority('editor')")
	@RequestMapping("/admin/delete/{id}")
	public String delete(@PathVariable long id) {
		itemRepository.deleteById(id);
		return "redirect:/admin/list?deleted=" + id;
	}

}
