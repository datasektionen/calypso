package se.datasektionen.calypso.feeds;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.property.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import se.datasektionen.calypso.models.entities.ActivityPeriod;
import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.repositories.ActivityPeriodRepository;
import se.datasektionen.calypso.models.repositories.ApiRepository;
import se.datasektionen.calypso.Darkmode;

import static se.datasektionen.calypso.feeds.DateUtils.applyRecurrence;
import static se.datasektionen.calypso.feeds.DateUtils.ldtToDate;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class IcalFeed {

	private static final String CALENDAR_NAME = "Datasektionen";
	private static final Color CALENDAR_COLOR = new Color("hotpink");
	private static final String CALENDAR_SV_PRODUCT_ID = "-//Datasektionen//Calypso//SV";
	private static final String CALENDAR_EN_PRODUCT_ID = "-//Datasektionen//Calypso//EN";

	private final ApiRepository apiRepository;
	private final Darkmode darkmode;
	private final ActivityPeriodRepository activityPeriodRepository;

	public String renderIcsFeed(boolean english) {
		var ical = new ICalendar();
		ical.setName(CALENDAR_NAME);
		ical.setColor(CALENDAR_COLOR);
		ical.setProductId(english ? CALENDAR_EN_PRODUCT_ID : CALENDAR_SV_PRODUCT_ID);

		var tz = TimeZone.getTimeZone("Europe/Stockholm");
		var assignment = new TimezoneAssignment(tz, "Europe/Stockholm");
		ical.getTimezoneInfo().setDefaultTimezone(assignment);

		var now = LocalDateTime.now();
		var isReception = darkmode.getCurrent();

		apiRepository.eventsInTimeSpan(now.minusMonths(2), now.plusYears(2), isReception)
				.stream()
				.map(e -> toEvent(e, english))
				.forEach(ical::addEvent);

		activityPeriodRepository.findAllAfter(now.toLocalDate().minusMonths(2), isReception)
				.stream()
				.map(p -> toEvent(p, english))
				.forEach(ical::addEvent);

		return Biweekly.write(ical).go();
	}

	private static VEvent toEvent(Item e, boolean english) {
		var event = new VEvent();
		event.setUid(e.getId().toString());
		event.setLocation(e.getEventLocation());
		event.setDateStart(ldtToDate(e.getEventStartTime()));
		event.setDateEnd(ldtToDate(e.getEventEndTime()));

		if (english) {
			event.setSummary(e.getTitleEnglish());
			event.setDescription(e.getContentEnglish());
		} else {
			event.setSummary(e.getTitleSwedish());
			event.setDescription(e.getContentSwedish());
		}

		return event;
	}

	private static VEvent toEvent(ActivityPeriod period, boolean english) {
		var activity = period.getActivity();

		var event = new VEvent();
		event.setUid("A" + activity.getId().toString() + "P" + period.getId().toString());
		event.setLocation(period.getLocation());
		event.setDateStart(ldtToDate(period.getStartDate().atTime(period.getStartTime())));
		event.setDateEnd(ldtToDate(period.getStartDate().atTime(period.getEndTime())));

		applyRecurrence(
				event,
				period.getStartDate(),
				period.getEndDate(),
				period.getRecurrence(),
				period.getStartTime());

		if (english) {
			event.setSummary(activity.getTitleEnglish());
			event.setDescription(activity.getContentEnglish());
		} else {
			event.setSummary(activity.getTitleSwedish());
			event.setDescription(activity.getContentSwedish());
		}

		return event;
	}

}
