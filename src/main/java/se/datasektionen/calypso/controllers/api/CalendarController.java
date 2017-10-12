package se.datasektionen.calypso.controllers.api;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.datasektionen.calypso.models.enums.ItemType;
import se.datasektionen.calypso.models.repositories.ItemRepository;

import static se.datasektionen.calypso.util.DateUtils.ldtToDate;

@Controller
public class CalendarController {

	private ItemRepository itemRepository;

	@Autowired
	public CalendarController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping(produces = "text/calendar", method = RequestMethod.GET, value = "/feeds/ical")
	@ResponseBody
	public String eventFeed() {
		PageRequest pageable = new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "eventStartTime"));
		ICalendar ical = new ICalendar();

		itemRepository
				.findAllByItemType(ItemType.EVENT, pageable)
				.getContent()
				.stream()
				.map(e -> {
					VEvent event = new VEvent();
					event.setUid(e.getId().toString());
					event.setSummary(e.getTitleSwedish());
					event.setDescription(e.getContentSwedish());
					event.setDateStart(ldtToDate(e.getEventStartTime()));
					event.setDateEnd(ldtToDate(e.getEventEndTime()));
					return event;
				})
				.forEach(ical::addEvent);

		return Biweekly.write(ical).go();
	}

}
