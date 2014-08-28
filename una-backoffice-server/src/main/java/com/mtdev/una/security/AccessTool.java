package com.mtdev.una.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("AccessTool")
public class AccessTool {

	public boolean alwaysOk() {
		return true;
	}

	public boolean isOk() {
		return isOk(null);
	}

	public boolean isOk(String pInput) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		/*
		 * User user = SecurityUtil.getUserCredential(); HashMap<String,
		 * PrivilegeResult> pMap =user.getPrivilegeMap(); PrivilegeResult
		 * privResult = pMap.get(permission); hasPermission =
		 * privResult.isAllowAccess();
		 */

		return auth.getName().contains("mik");
	}

	public boolean isAuthenticated() {
		Authentication lAuth = SecurityContextHolder.getContext()
				.getAuthentication();

		return lAuth.getName() != null && !StringUtils.isEmpty(lAuth.getName())
				&& !lAuth.getName().contains("anonymousUser");
	}

	public Object getUserDetails() {
		Authentication lAuth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (lAuth != null)
			return lAuth.getPrincipal();
		else
			return null;
	}

	public String getUsername() {
		Authentication lAuth = SecurityContextHolder.getContext()
				.getAuthentication();
		try {
			UserDetailsImpl lUserDetailsImpl = (UserDetailsImpl) getUserDetails();
			if (lAuth != null)
				return lUserDetailsImpl.getUsername();
		} catch (Exception lE) {

		}

		return null;
	}

	public List<String> getUserRoles() {
		List<String> lRoles = new ArrayList<String>();
		if (this.isAuthenticated()) {
			Authentication lAuth = SecurityContextHolder.getContext()
					.getAuthentication();
			Collection<? extends GrantedAuthority> lAuthorities = lAuth
					.getAuthorities();

			for (GrantedAuthority lAuthority : lAuthorities) {
				lRoles.add(lAuthority.getAuthority());
			}

		}

		return lRoles;
	}

}
