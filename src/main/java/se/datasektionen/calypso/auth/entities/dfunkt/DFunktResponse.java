
package se.datasektionen.calypso.auth.entities.dfunkt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DFunktResponse {

	@JsonProperty("mandates")
	private List<Mandate> mandates = null;

	@JsonProperty("mandates")
	public List<Mandate> getMandates() {
		return mandates;
	}

	@JsonProperty("mandates")
	public void setMandates(List<Mandate> mandates) {
		this.mandates = mandates;
	}

	@Override
	public String toString() {
		return "DFunktResponse{" +
				"mandates=" + mandates +
				'}';
	}
}
