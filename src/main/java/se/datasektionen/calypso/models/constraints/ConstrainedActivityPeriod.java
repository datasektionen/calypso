package se.datasektionen.calypso.models.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Constraint(validatedBy = ActivityPeriodValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainedActivityPeriod {
    String message() default "Perioden måste ha både start- och sluttid samt en plats.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
