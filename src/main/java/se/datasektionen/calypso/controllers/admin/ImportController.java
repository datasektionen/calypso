package se.datasektionen.calypso.controllers.admin;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.exceptions.BadRequestException;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ItemRepository;
import se.datasektionen.calypso.util.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static se.datasektionen.calypso.util.StringUtils.facebookEvent;
import static se.datasektionen.calypso.util.StringUtils.getBodyText;

@Controller
public class ImportController {

	private String callbackURL = "http://localhost:8080/admin/import/authorize";
	private ItemRepository itemRepository;
	private DateTimeFormatter formatter;

	@Autowired
	public ImportController(ItemRepository itemRepository, DateTimeFormatter formatter) {
		this.itemRepository = itemRepository;
		this.formatter = formatter;
	}

	@RequestMapping(value = "/admin/import", method = RequestMethod.GET)
	public String facebookImport(Authentication auth, Model model) {
		DAuthUserDetails user = (DAuthUserDetails) auth.getPrincipal();

		// Catch unauthenticated users
		if (user.getFacebook() == null)
			return "redirect:" + new FacebookFactory().getInstance().getOAuthAuthorizationURL(callbackURL);

		return "import";
	}

	@RequestMapping(value = "/admin/import/authorize", method = RequestMethod.GET)
	public String authorizeFacebook(@RequestParam(name = "code", required = false) String code, Authentication auth) {
		DAuthUserDetails user = (DAuthUserDetails) auth.getPrincipal();

		try {
			Facebook facebook = new FacebookFactory().getInstance();
			facebook.getOAuthAccessToken(code, callbackURL);
			user.setFacebook(facebook);

			return "redirect:/admin/import";
		} catch (FacebookException e) {
			e.printStackTrace();
			throw new BadRequestException();
		}
	}

	@RequestMapping(value = "/admin/import", method = RequestMethod.POST)
	public Object doFacebookImport(@RequestParam("url") String url,
	                               Authentication auth, Model model) {
		DAuthUserDetails user = (DAuthUserDetails) auth.getPrincipal();
		String eventId;
		try {
			eventId = facebookEvent(url);

			Event event = user.getFacebook()
					.events()
					.getEvent(eventId);

			Item item = new Item(
					ItemType.EVENT,
					event.getName(),
					event.getName(),
					getBodyText(event.getDescription(), true),
					getBodyText(event.getDescription(), false),
					user.getUser(),
					event.getLocation(),
					DateUtils.dateToLDT(event.getStartTime()),
					DateUtils.dateToLDT(event.getEndTime()),
					"https://facebook.com/events/" + eventId + "/"
			);
			item.setAuthorDisplay(user.getName());

			model.addAttribute("item", item);
			model.addAttribute("now", LocalDateTime.now().format(formatter));
			model.addAttribute("formatter", formatter);

			return "edit";
		} catch (IllegalArgumentException e) {
			return "redirect:/admin/import?error=Invalid event URL";
		} catch (FacebookException e) {
			e.printStackTrace();
			return "redirect:/admin/import?error=Facebook Error:" + e.getMessage();
		}
	}

}
