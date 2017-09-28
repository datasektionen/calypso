
package se.datasektionen.calypso.auth.entities.dfunkt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"start",
		"end",
		"Role"
})
public class Mandate {

	@JsonProperty("start")
	private String start;
	@JsonProperty("end")
	private String end;
	@JsonProperty("Role")
	private Role role;

	@JsonProperty("start")
	public String getStart() {
		return start;
	}

	@JsonProperty("start")
	public void setStart(String start) {
		this.start = start;
	}

	@JsonProperty("end")
	public String getEnd() {
		return end;
	}

	@JsonProperty("end")
	public void setEnd(String end) {
		this.end = end;
	}

	@JsonProperty("Role")
	public Role getRole() {
		return role;
	}

	@JsonProperty("Role")
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Mandate{" +
				"start='" + start + '\'' +
				", end='" + end + '\'' +
				", role=" + role +
				'}';
	}
}
