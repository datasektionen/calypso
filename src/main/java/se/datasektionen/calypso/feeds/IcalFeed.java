package se.datasektionen.calypso.feeds;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ApiRepository;

import static se.datasektionen.calypso.feeds.DateUtils.ldtToDate;

@Service
@RequiredArgsConstructor
public class IcalFeed {

	private static final String CALENDAR_NAME = "Datasektionen";
	private static final Color CALENDAR_COLOR = new Color("hotpink");
	private static final String CALENDAR_PRODUCT_ID = "-//Datasektionen//Calypso//SV";

	private final ApiRepository apiRepository;

	public String renderIcsFeed() {
		var ical = new ICalendar();
		ical.setName(CALENDAR_NAME);
		ical.setColor(CALENDAR_COLOR);
		ical.setProductId(CALENDAR_PRODUCT_ID);

		apiRepository
				.allEvents()
				.stream()
				.filter(e -> e.getEventStartTime() != null && e.getEventEndTime() != null)
				.map(IcalFeed::toEvent)
				.forEach(ical::addEvent);

		return Biweekly.write(ical).go();
	}

	private static VEvent toEvent(Item e) {
		var event = new VEvent();
		event.setUid(String.valueOf(e.getId()));
		event.setSummary(e.getTitleSwedish());
		event.setDescription(e.getContentSwedish());
		event.setDateStart(ldtToDate(e.getEventStartTime()));
		event.setDateEnd(ldtToDate(e.getEventEndTime()));
		event.setLocation(e.getEventLocation());
		return event;
	}

}
