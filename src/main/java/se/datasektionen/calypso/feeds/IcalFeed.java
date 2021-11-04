package se.datasektionen.calypso.feeds;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.models.repositories.ReceptionRepository;

import static se.datasektionen.calypso.feeds.DateUtils.ldtToDate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IcalFeed {

	private static final String CALENDAR_NAME = "Datasektionen";
	private static final Color CALENDAR_COLOR = new Color("hotpink");
	private static final String CALENDAR_SV_PRODUCT_ID = "-//Datasektionen//Calypso//SV";
	private static final String CALENDAR_EN_PRODUCT_ID = "-//Datasektionen//Calypso//EN";

	private final ApiRepository apiRepository;
	private final ReceptionRepository receptionRepository;

	public String renderIcsFeedSwedish() {
		var ical = new ICalendar();
		ical.setName(CALENDAR_NAME);
		ical.setColor(CALENDAR_COLOR);
		ical.setProductId(CALENDAR_SV_PRODUCT_ID);

		var isReception = receptionRepository.get();

		apiRepository
				.allEvents()
				.stream()
				.filter(e ->
					e.getEventStartTime() != null && e.getEventEndTime() != null &&
					LocalDateTime.now().minusMonths(2)
					.compareTo(e.getEventStartTime()) < 0 &&
					!(isReception.getState() && e.isSensitive())
				)
				.map(IcalFeed::toEventSwedish)
				.forEach(ical::addEvent);

		return Biweekly.write(ical).go();
	}

	public String renderIcsFeedEnglish() {
		var ical = new ICalendar();
		ical.setName(CALENDAR_NAME);
		ical.setColor(CALENDAR_COLOR);
		ical.setProductId(CALENDAR_EN_PRODUCT_ID);

		var isReception = receptionRepository.get();

		apiRepository
				.allEvents()
				.stream()
				.filter(e ->
					e.getEventStartTime() != null && e.getEventEndTime() != null &&
					LocalDateTime.now().minusMonths(2)
					.compareTo(e.getEventStartTime()) < 0 &&
					!(isReception.getState() && e.isSensitive())
				)
				.map(IcalFeed::toEventEnglish)
				.forEach(ical::addEvent);

		return Biweekly.write(ical).go();
	}


	private static VEvent toEventSwedish(Item e) {
		var event = new VEvent();
		event.setUid(String.valueOf(e.getId()));
		event.setSummary(e.getTitleSwedish());
		event.setDescription(e.getContentSwedish());
		event.setDateStart(ldtToDate(e.getEventStartTime()));
		event.setDateEnd(ldtToDate(e.getEventEndTime()));
		event.setLocation(e.getEventLocation());
		return event;
	}

	private static VEvent toEventEnglish(Item e) {
		var event = new VEvent();
		event.setUid(String.valueOf(e.getId()));
		event.setSummary(e.getTitleEnglish());
		event.setDescription(e.getContentEnglish());
		event.setDateStart(ldtToDate(e.getEventStartTime()));
		event.setDateEnd(ldtToDate(e.getEventEndTime()));
		event.setLocation(e.getEventLocation());
		return event;
	}

}
