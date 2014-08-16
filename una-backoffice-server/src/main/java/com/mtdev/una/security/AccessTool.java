package com.mtdev.una.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

}
