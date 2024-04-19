package se.datasektionen.calypso.config.convert;

import java.time.Period;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PeriodAttributeConverter implements AttributeConverter<Period, String> {

    @Override
    public String convertToDatabaseColumn(Period period) {
        return period.normalized().toString(); // ISO 8601
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        return Period.parse(dbData).normalized();
    }

}
