package com.mtdev.una.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import com.mtdev.una.data.dao.UserDao;
import com.mtdev.una.model.User;
import com.mtdev.una.tools.Constants.Security;

public class UserSeviceImpl implements UserDetailsService {

	@Autowired
	protected UserDao mUserDao;

	public UserDetails loadUserByUsername(String pUsername)
			throws UsernameNotFoundException, DataAccessException {

		UserDetails lUser = null;

		if (!StringUtils.isEmpty(pUsername)) {

			User lDbUser = mUserDao.getUserByUsername(pUsername);

			if (lDbUser != null) {
				List<GrantedAuthority> lAuthorities = new ArrayList<GrantedAuthority>();
				if (lDbUser.getRoles() != null && lDbUser.getRoles().size() > 0) {
					for (String lRole : lDbUser.getRoles()) {
						lAuthorities.add(new SimpleGrantedAuthority(lRole));
					}
				}

				lAuthorities.add(new SimpleGrantedAuthority(pUsername));

				StringBuilder lPasswords = null;
				if (lDbUser.getOpenids() != null
						&& lDbUser.getOpenids().size() > 0) {
					lPasswords = new StringBuilder();
					for (Entry<String, String> lOpenidEntry : lDbUser
							.getOpenids().entrySet()) {
						if (lPasswords.length() > 0)
							lPasswords.append(Security.sPasswordsSeparator);
						lPasswords.append(lOpenidEntry.getValue());
					}
				}

				lUser = new UserDetailsImpl(
						lDbUser.getUsername(),
						this.getUserPasswords(lDbUser.getPassword(), lPasswords),
						lAuthorities);

			}
		}

		if (lUser == null) {
			throw new UsernameNotFoundException("Cannot find the user object");
		}

		return lUser;
	}

	protected String getUserPasswords(String pPassword, StringBuilder pPasswords) {

		StringBuilder lPassword = new StringBuilder();

		if (pPassword != null)
			lPassword.append(pPassword);

		if (pPasswords != null && pPasswords.length() > 0) {
			if (lPassword.length() > 0)
				lPassword.append(Security.sPasswordsSeparator);

			lPassword.append(pPasswords);
		}

		return lPassword.toString();
	}

}
