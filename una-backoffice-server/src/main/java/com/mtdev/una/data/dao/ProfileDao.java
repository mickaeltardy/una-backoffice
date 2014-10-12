package com.mtdev.una.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.mtdev.una.model.Profile;

@Component
public class ProfileDao {

	@Autowired
	MongoOperations mMongoOperations;

	public Profile getProfileByUsername(String pUsername) {
		Query searchUserQuery = new Query(Criteria.where("username").is(
				pUsername));

		// find the saved user again.
		Profile lProfile = mMongoOperations.findOne(searchUserQuery,
				Profile.class);
		return lProfile;
	}

	public List<Profile> getAllProfiles() {

		// find the saved user again.
		List<Profile> lProfiles = mMongoOperations.findAll(Profile.class);
		return lProfiles;
	}

	public boolean saveProfile(Profile pProfile) {

		boolean lResult = false;
		if (pProfile != null) {
			try {
				mMongoOperations.save(pProfile);
				return true;
			} catch (Exception lE) {

			}
		}

		return lResult;

	}

}
