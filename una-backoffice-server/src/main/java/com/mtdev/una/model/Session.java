package com.mtdev.una.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sessions")
public class Session extends WorkoutImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3441947666303037956L;

}
