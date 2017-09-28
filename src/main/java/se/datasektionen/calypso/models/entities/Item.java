package se.datasektionen.calypso.models.entities;

import se.datasektionen.calypso.models.enums.ItemType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
public class Item {

	public enum PublishStatus { DRAFT, QUEUED, PUBLISHED }

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@NotNull
	private ItemType itemType;

	@Column(nullable = false)
	@NotNull
	@Size(min = 15, message = "Titel (svenska) måste vara minst 15 tecken")
	private String titleSwedish;

	@Column(nullable = false)
	@NotNull
	@Size(min = 15, message = "Titel (engelska) måste vara minst 15 tecken")
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
	private String image;

	@Column
	private boolean sticky;

	@Column
	private LocalDateTime publishDate;

	@Column(nullable = false)
	@NotNull
	@Size(min = 50, message = "Innehåll (svenska) måste vara minst 50 tecken")
	private String contentSwedish;

	@Column(nullable = false)
	@NotNull
	@Size(min = 50, message = "Innehåll (engelska) måste vara minst 50 tecken")
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public void setTitleSwedish(String titleSwedish) {
		this.titleSwedish = titleSwedish;
	}

	public void setTitleEnglish(String titleEnglish) {
		this.titleEnglish = titleEnglish;
	}

	public void setContentSwedish(String contentSwedish) {
		this.contentSwedish = contentSwedish;
	}

	public void setContentEnglish(String contentEnglish) {
		this.contentEnglish = contentEnglish;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setPublishDate(LocalDateTime publishDate) {
		this.publishDate = publishDate;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public void setEventStartTime(LocalDateTime eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public void setEventEndTime(LocalDateTime eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public void setFacebookEvent(String facebookEvent) {
		this.facebookEvent = facebookEvent;
	}

	public void setGoogleForm(String googleForm) {
		this.googleForm = googleForm;
	}

	public boolean isSticky() {
		return sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public String getAuthorDisplay() {
		return authorDisplay;
	}

	public String getPublishAs() {
		return publishAs;
	}

	public String getPublishAsDisplay() {
		return publishAsDisplay;
	}

	public void setAuthorDisplay(String authorDisplay) {
		this.authorDisplay = authorDisplay;
	}

	public void setPublishAs(String publishAs) {
		this.publishAs = publishAs;
	}

	public void setPublishAsDisplay(String publishAsDisplay) {
		this.publishAsDisplay = publishAsDisplay;
	}

	public PublishStatus getPublishStatus() {
		// Posts without an ID are always drafts
		if (this.getId() == null || this.getId() == 0 || this.getPublishDate() == null)
			return PublishStatus.DRAFT;

		LocalDateTime publishDate = this.getPublishDate();

		if (LocalDateTime.now().compareTo(publishDate) > 0)
			return PublishStatus.PUBLISHED;
		else
			return PublishStatus.QUEUED;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", itemType=" + itemType +
				", titleSwedish='" + titleSwedish + '\'' +
				", titleEnglish='" + titleEnglish + '\'' +
				", contentSwedish='" + contentSwedish + '\'' +
				", contentEnglish='" + contentEnglish + '\'' +
				", author='" + author + '\'' +
				", image='" + image + '\'' +
				", publishDate=" + publishDate +
				", eventLocation='" + eventLocation + '\'' +
				", eventStartTime=" + eventStartTime +
				", eventEndTime=" + eventEndTime +
				", facebookEvent='" + facebookEvent + '\'' +
				", googleForm='" + googleForm + '\'' +
				'}';
	}
}
