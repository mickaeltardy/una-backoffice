package com.mtdev.una.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.mtdev.una.model.User;

public class UserDetailsImpl extends
		org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2192398358888300263L;

	protected String mOrigin;

	public UserDetailsImpl(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}

	public UserDetailsImpl(User pUser,
			Collection<? extends GrantedAuthority> pAuthorities) {
		super(pUser.getUsername(), pUser.getPassword(), true, true, true, true,
				pAuthorities);
		this.setOrigin(null);
	}

	public UserDetailsImpl(String pUsername, String pPassword,
			Collection<? extends GrantedAuthority> pAuthorities) {
		super(pUsername, pPassword, true, true, true, true, pAuthorities);
		this.setOrigin(null);
	}

	public String getOrigin() {
		return mOrigin;
	}

	public void setOrigin(String pOrigin) {
		mOrigin = pOrigin;
	}

}
