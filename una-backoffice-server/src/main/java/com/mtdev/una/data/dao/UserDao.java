package com.mtdev.una.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;

import com.mtdev.una.model.User;

@Controller
public class UserDao {

	@Autowired
	MongoOperations mMongoOperations;

	public User getUserByUsername(String pUsername) {
		Query searchUserQuery = new Query(Criteria.where("username").is(
				pUsername));

		// find the saved user again.
		User lDbUser = mMongoOperations.findOne(searchUserQuery, User.class);
		return lDbUser;
	}

	public boolean saveUser(User pUser) {

		boolean lResult = false;
		if (pUser != null) {
			try {
				mMongoOperations.save(pUser);
				return true;
			} catch (Exception lE) {

			}
		}

		return lResult;

	}

}
