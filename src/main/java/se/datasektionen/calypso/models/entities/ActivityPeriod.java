package se.datasektionen.calypso.models.entities;

import lombok.Data;
import se.datasektionen.calypso.acl.SecurityTarget;
import se.datasektionen.calypso.config.convert.PeriodAttributeConverter;
import se.datasektionen.calypso.models.constraints.ConstrainedActivityPeriod;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

@Entity
@ConstrainedActivityPeriod
@Data
public class ActivityPeriod implements SecurityTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    @JsonIgnore
    @NotNull
    private Activity activity;

    @Column(nullable = false)
    @NotBlank
    private String name; // internal reference only

    @Column(nullable = false)
    @NotNull
    private LocalDate startDate; // inclusive

    @Column(nullable = false)
    @NotNull
    private LocalDate endDate; // inclusive

    @Column(nullable = false)
    @Convert(converter = PeriodAttributeConverter.class)
    @NotNull
    private Period recurrence; // between each event instance

    @Column(nullable = false)
    @NotNull
    private LocalTime startTime; // for each event instance

    @Column(nullable = false)
    @NotNull
    private LocalTime endTime; // for each event instance

    @Column(nullable = false)
    @NotBlank
    private String location;

    @JsonGetter("active")
    public boolean isActive(LocalDate when) {
        if (when == null) {
            when = LocalDate.now();
        }
        return !this.startDate.isAfter(when) && !this.endDate.isBefore(when);
    }

    @Override
    public String getAuthor() {
        return this.activity.getAuthor();
    }

}
