package com.mtdev.una.business.interfaces;

import java.util.List;

import com.mtdev.una.model.User;

public interface UsersManager {

	public User getUserByUsername(String pUsername);
	
	public boolean doesUserExist(String pUsername);

	public User createUser(String pUsername, String pPassword, boolean pSave);

	public User createUser(String pUsername, String pPassword,
			List<String> pRoles, boolean pSave);

	public boolean saveUser(User pUser);

}
