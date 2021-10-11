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
@Event
@EntityListeners(ItemListener.class)
@Data
public class Item {

	@Transient
	@JsonIgnore
	private static final Parser PARSER = Parser.builder().build();

	@Transient
	@JsonIgnore
	private static final HtmlRenderer RENDERER = HtmlRenderer.builder().escapeHtml(false).build();

	public enum PublishStatus { DRAFT, QUEUED, PUBLISHED }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	@Column(nullable = false)
	private LocalDateTime updated;

	@Column(nullable = false)
	@NotNull
	@Size(min = 8, message = "Titel (svenska) måste vara minst åtta tecken")
	private String titleSwedish;

	@Column(nullable = false)
	@NotNull
	@Size(min = 8, message = "Titel (engelska) måste vara minst åtta tecken")
	private String titleEnglish;

	@Column(nullable = false)
	@NotNull
	@Size(min = 3, message = "KTH ID på författare krävs")
	private String author;

	@Column(nullable = false)
	@NotNull
	@Size(min = 3, message = "Namn på författare krävs")
	private String authorDisplay;

	@Column
	private String publishAs;

	@Column
	private String publishAsDisplay;

	@Column
	private boolean sticky;

	// Sensitive = true means that this item will be hidden during the reception
	@Column(nullable = false)
	@NotNull
	private boolean sensitive;

	@Column
	private LocalDateTime publishDate;

	@Column(nullable = false, length = 10000)
	@NotNull
	@Size(min = 50, max = 9999, message = "Innehåll (svenska) måste vara minst 50 tecken")
	private String contentSwedish;

	@Column(nullable = false, length = 10000)
	@NotNull
	@Size(min = 50, max = 9999, message = "Innehåll (engelska) måste vara minst 50 tecken")
	private String contentEnglish;

	@Column
	private String eventLocation;

	@Column
	private LocalDateTime eventStartTime;

	@Column
	private LocalDateTime eventEndTime;

	@Column
	private String facebookEvent;

	@Column
	private String googleForm;

	@Enumerated(EnumType.STRING)
	public ItemType getItemType() {
		return itemType;
	}

	@JsonGetter("contentSwedish")
	public String getContentSwedishProcessed() {
		return RENDERER.render(PARSER.parse(contentSwedish)).replace("\\", "");
	}

	@JsonGetter("contentEnglish")
	public String getContentEnglishProcessed() {
		return RENDERER.render(PARSER.parse(contentEnglish)).replace("\\", "");
	}

	public boolean isSticky() {
		return sticky;
	}

	public boolean isSensitive() {
		return sensitive;
	}

	public PublishStatus getPublishStatus() {
		// Posts without an ID are always drafts
		if (this.getId() == null || this.getPublishDate() == null)
			return PublishStatus.DRAFT;

		var publishDate = this.getPublishDate();

		if (LocalDateTime.now().compareTo(publishDate) > 0)
			return PublishStatus.PUBLISHED;
		else
			return PublishStatus.QUEUED;
	}

	public void triggerUpdated() {
		this.updated = LocalDateTime.now();
	}

}
