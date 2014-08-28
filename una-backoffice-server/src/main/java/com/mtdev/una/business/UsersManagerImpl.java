package com.mtdev.una.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.UsersManager;
import com.mtdev.una.data.dao.UserDao;
import com.mtdev.una.model.User;
import com.mtdev.una.security.SecurityToolbox;
import com.mtdev.una.tools.Toolbox;

@Component
public class UsersManagerImpl implements UsersManager {

	@Autowired
	protected UserDao mUserDao;

	@Autowired
	protected SecurityToolbox mSecurityToolbox;

	@Override
	public User getUserByUsername(String pUsername) {
		return mUserDao.getUserByUsername(pUsername);
	}

	@Override
	public boolean doesUserExist(String pUsername) {
		return getUserByUsername(pUsername) != null;

	}

	@Override
	public User createUser(String pUsername, String pPassword, boolean pSave) {
		String lPassword = (pPassword != null) ? Toolbox.md5Spring(pPassword)
				: null;
		User lUser = new User(pUsername, lPassword);
		lUser.setRoles(mSecurityToolbox.getBasicRoles());
		if ((pSave && saveUser(lUser)) || pSave)
			return lUser;
		else
			return null;
	}

	@Override
	public User createUser(String pUsername, String pPassword,
			List<String> pRoles, boolean pSave) {
		User lUser = new User(pUsername, pPassword);
		lUser.setRoles(pRoles);
		if ((pSave && saveUser(lUser)) || pSave)
			return lUser;
		else
			return null;
	}

	@Override
	public boolean saveUser(User pUser) {

		return mUserDao.saveUser(pUser);
	}

}
