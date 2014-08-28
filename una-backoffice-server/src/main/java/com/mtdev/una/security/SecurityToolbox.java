package com.mtdev.una.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityToolbox {

	@Autowired
	AccessTool mAccessTool;

	public boolean isUserRoleOnly() {

		boolean lResult = true;
		List<String> lRoles = mAccessTool.getUserRoles();
		String lUsername = mAccessTool.getUsername();
		if (lRoles == null || lRoles.size() == 0)
			lResult = false;
		else
			for (String lRole : lRoles) {
				if (lRole.compareTo(SecurityConstants.Roles.user) != 0
						&& lRole.compareTo(lUsername) != 0) {
					lResult = false;
					break;
				}
			}

		return lResult;
	}

	public String getUsername() {
		return mAccessTool.getUsername();
	}

	public List<String> getBasicRoles() {
		List<String> lRoles = new ArrayList<String>();

		lRoles.add(SecurityConstants.Roles.user);
		return lRoles;
	}

	public boolean isAdminRole() {
		List<String> lRoles = mAccessTool.getUserRoles();

		return (lRoles != null && lRoles.size() > 0 && lRoles
				.contains(SecurityConstants.Roles.admin));

	}
}
