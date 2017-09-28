
package se.datasektionen.calypso.auth.entities.dfunkt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"title",
		"identifier",
		"email"
})
public class Role {

	@JsonProperty("title")
	private String title;
	@JsonProperty("identifier")
	private String identifier;
	@JsonProperty("email")
	private String email;

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("identifier")
	public String getIdentifier() {
		return identifier;
	}

	@JsonProperty("identifier")
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Role{" +
				"title='" + title + '\'' +
				", identifier='" + identifier + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
