package com.mtdev.una.business.interfaces;

import java.util.Map;

import com.mtdev.una.model.Profile;

public interface ProfilesManager {

	Profile getProfileByUsername(String pUsername);

	Profile createProfile(String pUsername, String pName, String pSurname,
			boolean pSave);

	boolean saveProfile(Map<Object, Object> pData);

	boolean saveProfileAndUser(Map<Object, Object> pProfileInput);

	boolean saveProfile(Profile pProfile);

}
