package se.datasektionen.calypso.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
public class DAuthUserDetails implements UserDetails {
	private static final GrantedAuthority EDITOR_AUTH = new SimpleGrantedAuthority("manage-all");

	private String user;
	private String token;
	private String firstName;
	private String lastName;
	private String email;
	private Map<String, String> mandates;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getPassword() {
		return token;
	}

	@Override
	public String getUsername() {
		return user;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public boolean isEditor() {
		return this.getAuthorities().contains(EDITOR_AUTH);
	}

}
