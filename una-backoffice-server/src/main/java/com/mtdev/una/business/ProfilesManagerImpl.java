package com.mtdev.una.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
			lProfile.setBirthdate((String)pData.get("birthdate"));
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

	public boolean validateData(Map<Object, Object> pData) {

		return Toolbox.mapContainsKeys(pData, getRequiredFields());
	}

	public Map<Object, Object> cleanupData(Map<Object, Object> pData) {
		Map<Object, Object> lCleanData = new HashMap<Object, Object>();
		
		Object[] lFieldsToRemove = getStandardFields();
		for (Entry<Object, Object> lEntry : pData.entrySet()){
			boolean lKeep = true;
			
			for (Object lObject : lFieldsToRemove) {
				if(((String)lObject).compareTo((String)lEntry.getKey()) == 0){
					lKeep = false;
					break;
				}
			}
			if(lKeep)
				lCleanData.put(lEntry.getKey(), lEntry.getValue());
		}

		return lCleanData;
	}

	public Object[] getStandardFields() {
		Object[] lArray = getRequiredFields();
		lArray = ArrayUtils.addAll(lArray, "docsToProvide", "id", "passwordConfirmation", "username", "password");
		
				

		return lArray;
	}
	

	public Object[] getRequiredFields() {
		Object[] lArray = {  "username",
				"name", "surname", "sex", "birthdate",
				"nationality", "address", "zipcode", "city", "telephone" };

		return lArray;
	}

}
