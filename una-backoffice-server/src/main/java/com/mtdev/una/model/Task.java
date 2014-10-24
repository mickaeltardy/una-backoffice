package com.mtdev.una.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tasks")
public class Task extends WorkoutImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262096650048411638L;


	
}
