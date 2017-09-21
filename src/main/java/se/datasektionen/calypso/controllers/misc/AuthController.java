package se.datasektionen.calypso.controllers.misc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

	@RequestMapping("/auth/login")
	public String login() {
		String dLoginUrl = "http://login2.datasektionen.se/login?callback=";
		String calypsoUrl = "http://localhost:8080/auth/login?token=";

		return "redirect:" + dLoginUrl + calypsoUrl;
	}

}
