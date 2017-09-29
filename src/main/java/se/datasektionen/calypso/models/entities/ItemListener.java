package se.datasektionen.calypso.models.entities;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ItemListener {

	@PrePersist
	protected void onCreate(Item item) {
		item.triggerCreated();
		item.triggerUpdated();
	}

	@PreUpdate
	protected void onUpdate(Item item) {
		item.triggerUpdated();
	}
}
