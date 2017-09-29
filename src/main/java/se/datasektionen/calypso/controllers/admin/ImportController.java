package se.datasektionen.calypso.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static se.datasektionen.calypso.util.StringUtils.facebookEvent;

@Controller
public class ImportController {

	@RequestMapping(value = "/admin/import", method = RequestMethod.GET)
	public String facebookImport(@RequestParam(name = "error", required = false) String error, Model model) {
		model.addAttribute("error", error);
		return "import";
	}

	@ResponseBody
	@RequestMapping(value = "/admin/import", method = RequestMethod.POST)
	public Object doFacebookImport(@RequestParam("url") String url, Model model) {
		String eventId;
		try {
			eventId = facebookEvent(url);
		} catch (IllegalArgumentException e) {
			return "redirect:/admin/import?error=Invalid event URL";
		}

		// return facebook.eventOperations().getEvent(eventId);
		return "import";
	}

}
