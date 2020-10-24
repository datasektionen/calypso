package se.datasektionen.calypso.feeds.rss;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public final class RssView extends AbstractRssFeedView {

	private static final String RSS_2 = "rss_2.0";

	private final List<Item> items;
	private final String url;
	private final String title;
	private final String description;

	@Override
	protected Channel newFeed() {
		var channel = new Channel(RSS_2);
		channel.setLink(url);
		channel.setTitle(title);
		channel.setDescription(description);
		return channel;
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return items;
	}
}
