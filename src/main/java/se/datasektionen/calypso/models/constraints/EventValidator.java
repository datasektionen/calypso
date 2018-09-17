package se.datasektionen.calypso.models.constraints;

import se.datasektionen.calypso.models.entities.Item;
import se.datasektionen.calypso.models.enums.ItemType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventValidator implements ConstraintValidator<Event, Item> {

	@Override
	public void initialize(Event event) {}

	@Override
	public boolean isValid(Item item, ConstraintValidatorContext ctx) {
		// A valid item is *either* a post, which contains no event data at all,
		// *or* an event, which has to have both start time, end time and a venue
		return item.getItemType() == ItemType.POST ||
				item.getEventStartTime() != null
						&& item.getEventEndTime() != null
						&& item.getEventLocation() != null
						&& item.getEventLocation().length() > 1;
	}

}
