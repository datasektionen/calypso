package se.datasektionen.calypso.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.datasektionen.calypso.feeds.IcalFeed;
import se.datasektionen.calypso.feeds.rss.RssFeeds;
import se.datasektionen.calypso.feeds.rss.RssView;

@Controller
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final RssFeeds rssFeeds;
	private final IcalFeed icalFeed;

	@RequestMapping(produces = "text/calendar", method = RequestMethod.GET, value = "/ical")
	@ResponseBody
	public String eventFeedSwedish() {
		return icalFeed.renderIcsFeed(false);
	}

	@RequestMapping(produces = "text/calendar", method = RequestMethod.GET, value = "/ical_en")
	@ResponseBody
	public String eventFeed() {
		return icalFeed.renderIcsFeed(true);
	}

	@RequestMapping(produces = "application/rss+xml", method = RequestMethod.GET, value = "/rss")
	public RssView rssFeedSwedish() {
		return rssFeeds.swedishFeed();
	}

	@RequestMapping(produces = "application/rss+xml", method = RequestMethod.GET, value = "/rss_en")
	public RssView rssFeedEnglish() {
		return rssFeeds.englishFeed();
	}

}
