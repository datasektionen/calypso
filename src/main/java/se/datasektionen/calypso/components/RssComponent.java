package se.datasektionen.calypso.components;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.util.converters.RssConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("rssView")
public final class RssComponent extends AbstractRssFeedView {

	@Value("${application.base-url}")
	private String baseUrl;
	private ApiRepository apiRepository;

	@Autowired
	public RssComponent(ApiRepository apiRepository) {
		this.apiRepository = apiRepository;
	}

	@Override
	protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink(baseUrl + "/posts/feed/");
		channel.setTitle("Datasektionen.se - RSS");
		channel.setDescription("Nyheter och event från Datasektionen.se");
		return channel;
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return apiRepository
				.findAllPublished(new PageRequest(0, 100))
				.getContent()
				.stream()
				.map(RssConverter::toRssItem)
				.collect(Collectors.toList());
	}
}
