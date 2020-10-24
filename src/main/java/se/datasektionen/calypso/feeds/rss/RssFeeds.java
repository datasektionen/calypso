package se.datasektionen.calypso.feeds.rss;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.config.Config;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RssFeeds {

	private final ApiRepository apiRepository;
	private final Config config;

	public RssView swedishFeed() {
		return new RssView(fetchAndConvertSwedishItems(),
				config.getBaseUrl() + RssConstants.Swedish.FEED_URL,
				RssConstants.Swedish.TITLE,
				RssConstants.Swedish.DESCRIPTION);
	}

	public RssView englishFeed() {
		return new RssView(fetchAndConvertEnglishItems(),
				config.getBaseUrl() + RssConstants.English.FEED_URL,
				RssConstants.English.TITLE,
				RssConstants.English.DESCRIPTION);
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertSwedishItems() {
		return fetchAndConvertItems(Item::getTitleSwedish,
				Item::getContentSwedishProcessed,
				i -> RssConstants.Swedish.LINK_BASE_URL + i.getId());
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertEnglishItems() {
		return fetchAndConvertItems(Item::getTitleEnglish,
				Item::getContentEnglishProcessed,
				i -> RssConstants.English.LINK_BASE_URL + i.getId());
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertItems(Function<Item, String> titleMapper,
																		Function<Item, String> contentMapper,
																		Function<Item, String> linkMapper) {
		return apiRepository
				.findAllPublished(PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "publishDate")))
				.getContent()
				.stream()
				.map(i -> RssConverter.toRssItem(i, titleMapper, contentMapper, linkMapper))
				.collect(Collectors.toList());
	}

}
