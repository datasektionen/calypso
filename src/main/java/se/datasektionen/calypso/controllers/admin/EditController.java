package se.datasektionen.calypso.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ItemRepository;
import se.datasektionen.calypso.s3.S3Service;
import se.datasektionen.calypso.util.FileUtils;

import javax.validation.Valid;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@PreAuthorize("hasAuthority('post')")
@RequiredArgsConstructor
public class EditController {

	private final ItemRepository itemRepository;
	private final DateTimeFormatter formatter;
	private final S3Service s3Service;

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
	public String doEdit(@RequestParam(required = false) String publish,
			@RequestParam(required = false, value = "image") MultipartFile file, @Valid Item item,
			BindingResult bindingResult, Model model) {
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		// Check for form errors
		if (bindingResult.hasErrors())
			return "edit";

		// If the special publish param (name of a submit button) is present, we publish
		if (publish != null)
			item.setPublishDate(LocalDateTime.now());

		if (!file.isEmpty()) {
			try {
				// Implicitly check if file is an image
				BufferedImage image = FileUtils.convertFileToImage(file);

				int[] imageDimensions = FileUtils.getImageDimensions(image);

				if (!(imageDimensions[0] == 1920 && imageDimensions[1] == 1080)) {
					model.addAttribute("imageError", "Bildens dimensioner måste vara 1920x1080");
					return "edit";
				}

				String extension = FileUtils.getImageExtension(file);

				if (!extension.equals("image/png") && !extension.equals("image/jpeg")) {
					model.addAttribute("imageError", "Endast .png, .jpeg eller .jpg är tillåtna.");
					return "edit";
				}

				String nameExtension = "image/" + FileUtils.getNameExtension(file);

				if (!nameExtension.equals(extension)) {
					model.addAttribute("imageError", "Din filändelse matchar inte typen av din fil");
					return "edit";
				}

			} catch (IOException exception) {
				model.addAttribute("imageError", "Kunde inte läsa in bilden: " + exception);
				return "edit";
			}
		}

		item = itemRepository.save(item);

		if (!file.isEmpty()) {
			String extension = FileUtils.getNameExtension(file);

			String result = s3Service.uploadImage(file, extension, item.getId());

			item.setImageURL(result);
			item = itemRepository.save(item);
		}

		return "redirect:/admin/edit?saved=true&id=" + item.getId();
	}

	@PreAuthorize("hasAuthority('editor')")
	@RequestMapping("/admin/delete/{id}")
	public String delete(@PathVariable long id) {
		itemRepository.deleteById(id);
		return "redirect:/admin/list?deleted=" + id;
	}

}
