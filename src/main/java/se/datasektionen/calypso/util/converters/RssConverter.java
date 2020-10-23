package se.datasektionen.calypso.util.converters;

import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import se.datasektionen.calypso.models.entities.Item;

import static se.datasektionen.calypso.util.DateUtils.ldtToDate;

public class RssConverter {

	/**
	 * Convert a Item entity to an RSS item.
	 */
	public static com.rometools.rome.feed.rss.Item toRssItem(Item item) {
		com.rometools.rome.feed.rss.Item rssItem = new com.rometools.rome.feed.rss.Item();

		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(item.getContentSwedishProcessed());

		rssItem.setTitle(item.getTitleSwedish());
		rssItem.setDescription(description);
		rssItem.setPubDate(ldtToDate(item.getPublishDate()));
		rssItem.setLink("https://datasektionen.se/nyheter/" + item.getId());
		return rssItem;
	}

	/**
	 * Convert a Item entity to an English RSS item.
	 */
	public static com.rometools.rome.feed.rss.Item toEnglishRssItem(Item item) {
		com.rometools.rome.feed.rss.Item rssItem = new com.rometools.rome.feed.rss.Item();

		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(item.getContentEnglishProcessed());

		rssItem.setTitle(item.getTitleEnglish());
		rssItem.setDescription(description);
		rssItem.setPubDate(ldtToDate(item.getPublishDate()));
		rssItem.setLink("https://datasektionen.se/en/news/" + item.getId());
		return rssItem;
	}
}
