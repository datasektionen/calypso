package se.datasektionen.calypso.models.entities;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ReceptionListener {

	@PrePersist
	protected void onCreate(ReceptionMode r) {
		r.triggerUpdated();
	}

	@PreUpdate
	protected void onUpdate(ReceptionMode r) {
		r.triggerUpdated();
	}
}
