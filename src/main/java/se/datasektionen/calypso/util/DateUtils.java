package se.datasektionen.calypso.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	public static LocalDateTime dateToLDT(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date ldtToDate(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

}
