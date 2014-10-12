package com.mtdev.una.business;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mtdev.una.business.interfaces.ProfilesManager;
import com.mtdev.una.business.interfaces.UsersManager;
import com.mtdev.una.data.dao.ProfileDao;
import com.mtdev.una.model.Profile;
import com.mtdev.una.model.User;
import com.mtdev.una.tools.Toolbox;

@Component
public class ProfilesManagerImpl implements ProfilesManager {

	@Autowired
	protected ProfileDao mProfileDao;

	@Autowired
	protected UsersManager mUsersManager;

	@Override
	public Profile createProfile(String pUsername, String pName,
			String pSurname, boolean pSave) {
		Profile lProfile = new Profile(pUsername);
		lProfile.setName(pName);
		lProfile.setSurname(pSurname);

		if ((pSave && mProfileDao.saveProfile(lProfile)) || !pSave)
			return lProfile;
		else
			return null;

	}

	@Override
	public boolean saveProfile(Map<Object, Object> pData) {
		boolean lResult = false;
		try {
			String lUsername = (String) pData.get("username");

			Profile lProfile = getProfileByUsername(lUsername);
			if (lProfile == null) {
				lProfile = new Profile();
				lProfile.setUsername((String) pData.get("username"));
			}
			lProfile.setAddress((String) pData.get("address"));
			lProfile.setBirthdate(parse((String) pData.get("birthdate")));
			lProfile.setCity((String) pData.get("city"));
			lProfile.setLicence((String) pData.get("licence"));
			lProfile.setName((String) pData.get("name"));
			lProfile.setNationality((String) pData.get("nationality"));
			lProfile.setSex((String) pData.get("sex"));
			lProfile.setSurname((String) pData.get("surname"));
			lProfile.setTelephone((String) pData.get("telephone"));
			lProfile.setZipcode((String) pData.get("zipcode"));
			lProfile.setData(cleanupData(pData));

			lResult = mProfileDao.saveProfile(lProfile);
		} catch (Exception lE) {

		}

		return lResult;
	}

	public static Date parse(String input) throws java.text.ParseException {

		// NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
		// things a bit. Before we go on we have to repair this.
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");

		// this is zero time so we need to add that TZ indicator for
		if (input.endsWith("Z")) {
			input = input.substring(0, input.length() - 1) + "GMT-00:00";
		} else {
			int inset = 6;

			String s0 = input.substring(0, input.length() - inset);
			String s1 = input.substring(input.length() - inset, input.length());

			input = s0 + "GMT" + s1;
		}

		return df.parse(input);

	}

	protected Date getDateFromString(Object pObject) {

		try {
			String lDateStr = (String) pObject;
			SimpleDateFormat lSdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat lSdf2 = new SimpleDateFormat("dd/MM/yyyy");
			Date lDate = null;
			try {
				lDate = lSdf1.parse(lDateStr);
			} catch (Exception lE) {

			}
			if (lDate == null) {
				try {
					lDate = lSdf2.parse(lDateStr);
				} catch (Exception lE) {

				}
			}
			return lDate;

		} catch (Exception lE) {

		}
		return null;
	}

	@Override
	public Profile getProfileByUsername(String pUsername) {
		return mProfileDao.getProfileByUsername(pUsername);
	}

	@Override
	public boolean saveProfileAndUser(Map<Object, Object> pProfileInput) {
		boolean lResult = false;
		try {
			String lUsername = (String) pProfileInput.get("username");
			String lPassword = (String) pProfileInput.get("password");
			if (!StringUtils.isEmpty(lUsername)
					&& !StringUtils.isEmpty(lPassword)) {

				User lUser = mUsersManager.createUser(lUsername, lPassword,
						true);

				if (lUser != null) {
					lResult = saveProfile(pProfileInput);
				}

			}
		} catch (Exception lE) {

		}

		return lResult;
	}

	@Override
	public boolean saveProfile(Profile pProfile) {
		return mProfileDao.saveProfile(pProfile);
	}

	@Override
	public Object updateProfile(String pUsername,
			Map<Object, Object> pProfileInput) {

		boolean lResult = false;

		Profile lProfile = getProfileByUsername(pUsername);

		for (Entry<Object, Object> pEntry : pProfileInput.entrySet()) {
			try {
				Field field = Profile.class.getDeclaredField((String) pEntry
						.getKey());
				field.setAccessible(true);
				field.set(lProfile, pEntry.getValue());
			} catch (Exception lE) {

			}
		}

		lResult = mProfileDao.saveProfile(lProfile);

		return lResult;
	}

	public boolean validateData(Map<Object, Object> pData) {

		return Toolbox.mapContainsKeys(pData, getRequiredFields());
	}

	public Map<Object, Object> cleanupData(Map<Object, Object> pData) {
		Map<Object, Object> lCleanData = new HashMap<Object, Object>();

		Object[] lFieldsToRemove = getStandardFields();
		for (Entry<Object, Object> lEntry : pData.entrySet()) {
			boolean lKeep = true;

			for (Object lObject : lFieldsToRemove) {
				if (((String) lObject).compareTo((String) lEntry.getKey()) == 0) {
					lKeep = false;
					break;
				}
			}
			if (((String) lEntry.getKey()).compareTo("certificate") == 0) {
				try {
					lCleanData.put(lEntry.getKey(),
							parse((String) lEntry.getValue()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lKeep)
				lCleanData.put(lEntry.getKey(), lEntry.getValue());
		}

		return lCleanData;
	}

	public Object[] getStandardFields() {
		Object[] lArray = getRequiredFields();
		lArray = ArrayUtils.addAll(lArray, "docsToProvide", "id",
				"passwordConfirmation", "username", "password");

		return lArray;
	}

	public Object[] getRequiredFields() {
		Object[] lArray = { "username", "name", "surname", "sex", "birthdate",
				"nationality", "address", "zipcode", "city", "telephone" };

		return lArray;
	}

}
