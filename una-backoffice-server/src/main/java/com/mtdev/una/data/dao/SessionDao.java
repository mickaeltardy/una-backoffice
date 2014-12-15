package com.mtdev.una.data.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mtdev.una.model.Session;

@Component
public class SessionDao extends WorkoutDao<Session> {

	@SuppressWarnings("unchecked")
	public List<String> getAthletesList() {

		List<String> lAthletes = mMongoTemplate.getCollection("sessions")
				.distinct("athleteId");
		return lAthletes;
	}

}
