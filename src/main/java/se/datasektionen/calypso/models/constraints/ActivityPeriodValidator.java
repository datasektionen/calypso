package se.datasektionen.calypso.models.constraints;

import se.datasektionen.calypso.models.entities.ActivityPeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ActivityPeriodValidator implements ConstraintValidator<ConstrainedActivityPeriod, ActivityPeriod> {

    @Override
    public void initialize(ConstrainedActivityPeriod period) {
    }

    @Override
    public boolean isValid(ActivityPeriod period, ConstraintValidatorContext ctx) {
        return period.getEndDate().isAfter(period.getStartDate())
                && period.getEndTime().isAfter(period.getStartTime())
                && !period.getRecurrence().isZero()
                && period.getStartDate()
                        .plus(period.getRecurrence())
                        .isAfter(period.getStartDate()) // Java supports negative Periods, but we don't
                && !period.getName().isEmpty()
                && !period.getLocation().isEmpty();
    }

}
