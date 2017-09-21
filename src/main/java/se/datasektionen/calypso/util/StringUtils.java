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

}
