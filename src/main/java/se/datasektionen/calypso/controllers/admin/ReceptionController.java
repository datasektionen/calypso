package se.datasektionen.calypso.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.datasektionen.calypso.models.entities.ReceptionMode;
import se.datasektionen.calypso.models.repositories.ReceptionRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@PreAuthorize("hasAuthority('reception')")
@RequiredArgsConstructor
public class ReceptionController {

	private final ReceptionRepository receptionRepository;
	private final DateTimeFormatter formatter;

    @GetMapping("/admin/reception")
    public String get(Authentication auth, Model model) {

        var receptionMode = receptionRepository.get();
        if (receptionMode == null) {
			var r = new ReceptionMode();
			r.setState(false);
			r.triggerUpdated();
			model.addAttribute("receptionMode", r);
            model.addAttribute("now", LocalDateTime.now().format(formatter));
            model.addAttribute("formatter", formatter);
            return "reception";
        }

        model.addAttribute("receptionMode", receptionMode);
        model.addAttribute("now", LocalDateTime.now().format(formatter));
        model.addAttribute("formatter", formatter);

        return "reception";
    }

	@PostMapping("/admin/reception/activate")
	public String set(@Valid ReceptionMode receptionMode, BindingResult bindingResult, Model model) {
		model.addAttribute("now", LocalDateTime.now().format(formatter));
		model.addAttribute("formatter", formatter);

		if (receptionMode == null) {
			var r = new ReceptionMode();
			r.setState(false);
			r.triggerUpdated();
			model.addAttribute("receptionMode", r);
			return "redirect:/admin/reception";
		}

		// Check for form errors
		if (bindingResult.hasErrors())
			return "reception";

		receptionMode.triggerUpdated();
			
		receptionMode = receptionRepository.save(receptionMode);
		model.addAttribute("receptionMode", receptionMode);

		return "redirect:/admin/reception";
	}

}
