package se.datasektionen.calypso.models.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Constraint(validatedBy = EventValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
	String message() default "Event måste ha både start- och sluttid samt en plats.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}