package se.datasektionen.calypso.acl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.io.Serializable;
import java.util.Optional;

public class CalypsoPermissionsEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication auth, Object maybeTarget, Object permission) {
		var user = (DefaultOidcUser) auth.getPrincipal();

		boolean canManageAll = user.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(authority -> authority.equals("manage-all"));

		// it may or may not be in an Optional
		if (maybeTarget instanceof Optional opt && opt.isPresent()) {
			maybeTarget = opt.get();
		}

		if (maybeTarget instanceof SecurityTarget target) {
			// Editors always have access
			if (canManageAll)
				return true;

			return user.getName().equals(target.getAuthor());
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
