package se.datasektionen.calypso.models.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import se.datasektionen.calypso.acl.SecurityTarget;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(ActivityListener.class)
@Data
public class Activity implements SecurityTarget {

	@Transient
	@JsonIgnore
	private static final Parser PARSER = Parser.builder().build();

	@Transient
	@JsonIgnore
	private static final HtmlRenderer RENDERER = HtmlRenderer.builder().escapeHtml(false).build();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime updated;

	@Column(nullable = false)
	@NotBlank
	@Size(min = 8, message = "Titel (svenska) måste vara minst åtta tecken")
	private String titleSwedish;

	@Column(nullable = false)
	@NotBlank
	@Size(min = 8, message = "Titel (engelska) måste vara minst åtta tecken")
	private String titleEnglish;

	@Column(nullable = false)
	@NotBlank
	@Size(min = 3, message = "KTH ID på författare krävs")
	private String author;

	@Column(nullable = false)
	@NotBlank
	@Size(min = 3, message = "Namn på författare krävs")
	private String authorDisplay;

	// sensitive = true means that this activity will be hidden during the reception
	@Column(nullable = false)
	@NotNull
	private boolean sensitive;

	@Column(nullable = false, length = 10000)
	@NotBlank
	@Size(min = 50, max = 9999, message = "Innehåll (svenska) måste vara minst 50 tecken")
	private String contentSwedish;

	@Column(nullable = false, length = 10000)
	@NotBlank
	@Size(min = 50, max = 9999, message = "Innehåll (engelska) måste vara minst 50 tecken")
	private String contentEnglish;

	@OneToMany(mappedBy = "activity", cascade = { CascadeType.ALL })
	@ToString.Exclude
	private List<ActivityPeriod> periods;

	@Column
	private String imageURL;

	@JsonGetter("contentSwedish")
	public String getContentSwedishProcessed() {
		return RENDERER.render(PARSER.parse(contentSwedish)).replace("\\", "");
	}

	@JsonGetter("contentEnglish")
	public String getContentEnglishProcessed() {
		return RENDERER.render(PARSER.parse(contentEnglish)).replace("\\", "");
	}

	@JsonGetter("active")
	public boolean isActive() {
		var when = LocalDate.now();
		return this.periods.stream().anyMatch(period -> period.isActive(when));
	}

	public void triggerUpdated() {
		this.updated = LocalDateTime.now();
	}
}
