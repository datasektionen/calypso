package se.datasektionen.calypso.feeds.rss;

import java.util.function.Function;

public class RssConstants {

	public static class Swedish {
		public static final String TITLE = "Datasektionen.se - RSS";
		public static final String DESCRIPTION = "Nyheter och event från Datasektionen.se";
		public static final Function<Long, String> LINKER = id -> "https://datasektionen.se/nyheter/" + id;
		public static final String FEED_URL = "/feed/rss";
	}

	public static class English {
		public static final String TITLE = "Datasektionen.se - RSS (English)";
		public static final String DESCRIPTION = "News and events from Datasektionen.se";
		public static final Function<Long, String> LINKER = id -> "https://datasektionen.se/nyheter/"
				+ id + "?lang=en";
		public static final String FEED_URL = "/feed/rss_en";
	}

}
