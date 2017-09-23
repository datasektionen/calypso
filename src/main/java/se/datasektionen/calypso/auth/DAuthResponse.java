package se.datasektionen.calypso.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DAuthResponse {
	private String first_name;
	private String last_name;
	private String emails;
	private String user;
	private String ugkthid;

	public DAuthResponse() {}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getEmails() {
		return emails;
	}

	public String getUser() {
		return user;
	}

	public String getUgkthid() {
		return ugkthid;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setUgkthid(String ugkthid) {
		this.ugkthid = ugkthid;
	}

	@Override
	public String toString() {
		return "DAuthResponse{" +
				"first_name='" + first_name + '\'' +
				", last_name='" + last_name + '\'' +
				", emails='" + emails + '\'' +
				", user='" + user + '\'' +
				", ugkthid='" + ugkthid + '\'' +
				'}';
	}
}