package se.datasektionen.calypso.models.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import se.datasektionen.calypso.models.constraints.Event;
import se.datasektionen.calypso.models.enums.ItemType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Data
public class ReceptionMode {

	@Transient
	@JsonIgnore
	private static final Parser PARSER = Parser.builder().build();

	@Transient
	@JsonIgnore
	private static final HtmlRenderer RENDERER = HtmlRenderer.builder().escapeHtml(false).build();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private boolean state;

    @Column(nullable = false)
	private LocalDateTime updated;

	public boolean getState() {
		return state;
	}

    public void triggerUpdated() {
		this.updated = LocalDateTime.now();
	}

}
