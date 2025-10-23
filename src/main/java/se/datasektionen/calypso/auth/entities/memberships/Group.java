
package se.datasektionen.calypso.auth.entities.memberships;

import lombok.Data;

@Data
public class Group {

	private String group_name;
	private String group_id;
	private String group_domain;
	private String tag_content;

}
