package se.datasektionen.calypso.feeds.rss;

import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import se.datasektionen.calypso.models.entities.Item;

import java.util.function.Function;

import static se.datasektionen.calypso.feeds.DateUtils.ldtToDate;

public class RssConverter {

	/**
	 * Convert a Item entity to an RSS item.
	 */
	public static com.rometools.rome.feed.rss.Item toRssItem(Item item,
															 Function<Item, String> titleMapper,
															 Function<Item, String> authorMapper,
															 Function<Item, String> contentMapper,
															 Function<Item, String> linkMapper) {
		var rssItem = new com.rometools.rome.feed.rss.Item();

		var description = new Description();
		description.setType(Content.HTML);
		description.setValue(contentMapper.apply(item));

		rssItem.setTitle(titleMapper.apply(item));
		rssItem.setAuthor(authorMapper.apply(item));
		rssItem.setDescription(description);
		rssItem.setPubDate(ldtToDate(item.getPublishDate()));
		rssItem.setLink(linkMapper.apply(item));
		return rssItem;
	}

}
