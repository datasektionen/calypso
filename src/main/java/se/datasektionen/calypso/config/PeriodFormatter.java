package se.datasektionen.calypso.config;

import java.time.Period;

public class PeriodFormatter {
    public static String format(Period period) {
        var years = period.getYears();
        var months = period.getMonths();
        var days = period.getDays();

        if (years == 1 && months == 0 && days == 0) {
            return "Varje år";
        } else if (years == 0 && months == 1 && days == 0) {
            return "Varje månad";
        } else if (years == 0 && months == 0 && days == 7) {
            return "Varje vecka";
        } else if (years == 0 && months == 0 && days == 1) {
            return "Varje dag";
        }

        var recurrence = new StringBuilder("Varje ");

        if (years > 0) {
            recurrence.append(years).append(" år");
            if (months > 0 && days > 0) {
                recurrence.append(", ");
            } else if (months > 0 || days > 0) {
                recurrence.append(" och ");
            }
        }

        if (months > 0) {
            recurrence.append(months);
            if (months == 1) {
                recurrence.append(" månad");
            } else {
                recurrence.append(" månader");
            }
            if (days > 0) {
                recurrence.append(" och ");
            }
        }

        if (days > 0) {
            if (days % 7 == 0) {
                var weeks = days / 7;
                recurrence.append(weeks);
                if (weeks == 1) {
                    recurrence.append(" vecka");
                } else {
                    recurrence.append(" veckor");
                }
            } else {
                recurrence.append(days);
                if (days == 1) {
                    recurrence.append(" dag");
                } else {
                    recurrence.append(" dagar");
                }
            }
        }

        return recurrence.toString();
    }
}
