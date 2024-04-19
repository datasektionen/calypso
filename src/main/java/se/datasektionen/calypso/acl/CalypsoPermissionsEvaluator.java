package se.datasektionen.calypso.acl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.datasektionen.calypso.auth.DAuthUserDetails;

import java.io.Serializable;
import java.util.Optional;

public class CalypsoPermissionsEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication auth, Object maybeTarget, Object permission) {
		var user = (DAuthUserDetails) auth.getPrincipal();

		// it may or may not be in an Optional
		if (maybeTarget instanceof Optional opt && opt.isPresent()) {
			maybeTarget = opt.get();
		}

		if (maybeTarget instanceof SecurityTarget target) {
			var editor = user.getAuthorities().contains(new SimpleGrantedAuthority("editor"));

			// Editors always have access
			if (editor)
				return true;

			return user.getUser().equals(target.getAuthor());
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		throw new UnsupportedOperationException(
				"TargetID based permission checking not implemented in this application.");
	}
}
