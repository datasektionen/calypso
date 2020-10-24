
package se.datasektionen.calypso.auth.entities.dfunkt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public class DFunktResponse {

	@JsonProperty("mandates")
	private List<Mandate> mandates;

}
