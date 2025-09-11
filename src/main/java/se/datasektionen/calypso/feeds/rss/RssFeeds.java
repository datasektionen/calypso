package se.datasektionen.calypso.feeds.rss;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.config.Config;
import se.datasektionen.calypso.Darkmode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class RssFeeds {

	private final ApiRepository apiRepository;
	private final Darkmode darkmode;
	private final Config config;

	public RssView swedishFeed(boolean important) {
		return new RssView(fetchAndConvertSwedishItems(important),
				config.getBaseUrl() + RssConstants.Swedish.FEED_URL,
				RssConstants.Swedish.TITLE,
				RssConstants.Swedish.DESCRIPTION);
	}

	public RssView englishFeed(boolean important) {
		return new RssView(fetchAndConvertEnglishItems(important),
				config.getBaseUrl() + RssConstants.English.FEED_URL,
				RssConstants.English.TITLE,
				RssConstants.English.DESCRIPTION);
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertSwedishItems(boolean important) {
		return fetchAndConvertItems(important,
				Item::getTitleSwedish,
				Item::getAuthorDisplay,
				Item::getContentSwedishProcessed,
				i -> RssConstants.Swedish.LINKER.apply(i.getId()),
				Item::getImageURL);
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertEnglishItems(boolean important) {
		return fetchAndConvertItems(important,
				Item::getTitleEnglish,
				Item::getAuthorDisplay,
				Item::getContentEnglishProcessed,
				i -> RssConstants.English.LINKER.apply(i.getId()),
				Item::getImageURL);
	}

	private List<com.rometools.rome.feed.rss.Item> fetchAndConvertItems(boolean important,
																		Function<Item, String> titleMapper,
																		Function<Item, String> authorMapper,
																		Function<Item, String> contentMapper,
																		Function<Item, String> linkMapper,
															 			Function<Item, String> imageURLMapper) {
		Stream<Item> items = apiRepository
				.findAllPublishedWithFilters(
						darkmode.getCurrent(),
						important,
						PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "publishDate")))
				.getContent()
				.stream();

		return items
				.map(i -> RssConverter.toRssItem(i, titleMapper, authorMapper, contentMapper, linkMapper, imageURLMapper))
				.collect(Collectors.toList());
	}

}
