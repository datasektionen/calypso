package se.datasektionen.calypso.models.entities;

import se.datasektionen.calypso.models.enums.ItemType;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
public class Item {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private ItemType itemType;

	@Column(nullable = false)
	private String titleSwedish;

	@Column(nullable = false)
	private String titleEnglish;

	@Column(nullable = false)
	private String contentSwedish;

	@Column(nullable = false)
	private String contentEnglish;

	@Column(nullable = false)
	private String author;

	@Column
	private String image;

	@Column
	private LocalDateTime publishDate;

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

	public Item() {}

	public Item(ItemType itemType, String titleSwedish, String titleEnglish, String contentSwedish, String contentEnglish, String author, String image) {
		this.itemType = itemType;
		this.titleSwedish = titleSwedish;
		this.titleEnglish = titleEnglish;
		this.contentSwedish = contentSwedish;
		this.contentEnglish = contentEnglish;
		this.author = author;
		this.image = image;
	}

	@Enumerated(EnumType.STRING)
	public ItemType getItemType() {
		return itemType;
	}

	public Long getId() {
		return id;
	}

	public String getTitleSwedish() {
		return titleSwedish;
	}

	public String getTitleEnglish() {
		return titleEnglish;
	}

	public String getContentSwedish() {
		return contentSwedish;
	}

	public String getContentEnglish() {
		return contentEnglish;
	}

	public String getAuthor() {
		return author;
	}

	public String getImage() {
		return image;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public String getFacebookEvent() {
		return facebookEvent;
	}

	public String getGoogleForm() {
		return googleForm;
	}

	public LocalDateTime getPublishDate() {
		return publishDate;
	}

	public LocalDateTime getEventStartTime() {
		return eventStartTime;
	}

	public LocalDateTime getEventEndTime() {
		return eventEndTime;
	}
}
