package se.datasektionen.calypso.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String facebookEvent(String url) {
		Pattern p = Pattern.compile("facebook.com\\/events\\/(\\d+)");
		Matcher m = p.matcher(url);

		if (!m.find())
			throw new IllegalArgumentException("Not a valid Facebook URL.");

		return m.group(1);
	}

	public static String getBodyText(String input, boolean swedish) {
		Pattern p = Pattern.compile("^(..)\\1{5,}.?$", Pattern.MULTILINE);
		Matcher m = p.matcher(input);

		if (!m.find())
			return input;

		// For Swedish text, return the text from the start to the delimiter
		// For English text, return the text from the delimiter to the end
		return swedish ? input.substring(0, m.start(0)).trim() : input.substring(m.end(0)).trim();
	}

}
