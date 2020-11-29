
package se.datasektionen.calypso.auth.entities.dfunkt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Mandate {

	private String start;

	private String end;

	@JsonProperty("Role")
	private Role role;

}
