package se.datasektionen.calypso.feeds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import biweekly.component.VEvent;
import biweekly.property.RecurrenceDates;
import biweekly.util.Frequency;
import biweekly.util.ICalDate;
import biweekly.util.Recurrence;

public class DateUtils {
	private record SimpleRecurrence(Frequency frequency, Integer interval) {
	};

	public static Date ldtToDate(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static void applyRecurrence(VEvent event, LocalDate start, LocalDate end, Period period,
			LocalTime startTime) {
		var maybeSimple = toSimpleRecurrence(period); // 99% of use cases
		if (maybeSimple.isPresent()) {
			var simple = maybeSimple.get();

			var rec = new Recurrence.Builder(simple.frequency())
					.interval(simple.interval())
					.until(ldtToDate(end.atStartOfDay()), false) // until is inclusive :)
					.build();
			event.setRecurrenceRule(rec);
			return;
		}

		// iCal standard (RFC5545 et al.) doesn't support this Period, so we need to
		// manually compute the dates and use that instead

		var dates = new RecurrenceDates();

		var cur = start;
		while (!cur.isAfter(end)) {
			dates.getDates().add(new ICalDate(ldtToDate(cur.atTime(startTime)), true));
			cur = cur.plus(period);
		}

		event.addRecurrenceDates(dates);
	}

	private static Optional<SimpleRecurrence> toSimpleRecurrence(Period period) {
		var years = period.getYears();
		var months = period.getMonths();
		var days = period.getDays();

		if (years > 0 && months == 0 && days == 0) {
			return Optional.of(new SimpleRecurrence(Frequency.YEARLY, years));
		} else if (years == 0 && months > 0 && days == 0) {
			return Optional.of(new SimpleRecurrence(Frequency.MONTHLY, months));
		} else if (years == 0 && months == 0 && days % 7 == 0) {
			return Optional.of(new SimpleRecurrence(Frequency.WEEKLY, days / 7));
		} else if (years == 0 && months == 0 && days > 0) {
			return Optional.of(new SimpleRecurrence(Frequency.DAILY, days / 7));
		} else {
			return Optional.empty();
		}
	}

}
