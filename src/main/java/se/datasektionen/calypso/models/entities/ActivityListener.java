package se.datasektionen.calypso.models.entities;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ActivityListener {

    @PrePersist
    protected void onCreate(Activity item) {
        item.triggerUpdated();
    }

    @PreUpdate
    protected void onUpdate(Activity item) {
        item.triggerUpdated();
    }
}
