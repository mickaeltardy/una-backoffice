package com.mtdev.una.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.DigestUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.mtdev.una.data.SpringMongoConfig;
import com.mtdev.una.model.User;

//import org.springframework.context.support.GenericXmlApplicationContext;

public class App {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		// For XML
		// ApplicationContext ctx = new
		// GenericXmlApplicationContext("SpringConfig.xml");

		// For Annotation
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx
				.getBean("mongoTemplate");

		User user = null;

		user = new User("mik", md5Spring("pass"));
		// save
		mongoOperation.save(user);

		user = new User("mik1", md5Spring("pass1"));
		// save
		mongoOperation.save(user);

		user = new User("mik2", md5Spring("pass2"));
		// save
		mongoOperation.save(user);

		user = new User("mik3", md5Spring("pass3"));
		// save
		mongoOperation.save(user);

		user = new User("mik4", md5Spring("pass4"));
		// save
		mongoOperation.save(user);

		// now user object got the created id.

		/*
		 * // query to search user Query searchUserQuery = new
		 * Query(Criteria.where("username").is("mkyong"));
		 * 
		 * // find the saved user again. User savedUser =
		 * mongoOperation.findOne(searchUserQuery, User.class);
		 * System.out.println("2. find - savedUser : " + savedUser);
		 * 
		 * // update password mongoOperation.updateFirst(searchUserQuery,
		 * Update.update("password", "new password"),User.class);
		 * 
		 * // find the updated user object User updatedUser =
		 * mongoOperation.findOne(searchUserQuery, User.class);
		 * 
		 * System.out.println("3. updatedUser : " + updatedUser);
		 * 
		 * // delete //mongoOperation.remove(searchUserQuery, User.class);
		 * 
		 * // List, it should be empty now. List<User> listUser =
		 * mongoOperation.findAll(User.class);
		 * System.out.println("4. Number of user = " + listUser.size());
		 */
	}

	public static String md5Spring(String text) {
		return DigestUtils.md5DigestAsHex(text.getBytes());
	}

}