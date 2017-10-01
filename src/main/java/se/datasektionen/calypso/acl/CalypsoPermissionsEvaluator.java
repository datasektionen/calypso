package se.datasektionen.calypso.acl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.models.entities.Item;

import java.io.Serializable;

public class CalypsoPermissionsEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication auth, Object target, Object permission) {
		DAuthUserDetails user = (DAuthUserDetails) auth.getPrincipal();

		if (target instanceof Item) {
			boolean editor = user.getAuthorities().contains(new SimpleGrantedAuthority("editor"));
			Item item = (Item) target;

			// Editors always have access
			if (editor)
				return true;

			// By now, we know the user isn't editor, thus they can only access their own content
			if (user.getUser().equals(item.getAuthor()))
				return true;
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		throw new UnsupportedOperationException("TargetID based permission checking not implemented in this application.");
	}
}
