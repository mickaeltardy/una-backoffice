package com.mtdev.una.controller.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.data.dao.SessionDao;
import com.mtdev.una.data.dao.WorkoutDao;
import com.mtdev.una.model.Session;

@Controller
@RequestMapping("/sessions")
public class SessionsService extends WorkoutService<Session> {

	@Autowired
	protected SessionDao mSessionDao;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object createNewWorkout(
			@RequestBody List<Session> pTasks) {

		return super.createNewWorkout(pTasks);

	}

	@RequestMapping(value = "/retrieve", method = RequestMethod.POST)
	public @ResponseBody Object retrieveWorkouts(
			@RequestBody Map<Object, Object> pRequest) {

		return getDao().getWorkoutsByStrictRequest(pRequest, Session.class);
	}

	@Override
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody Object removeWorkouts(@RequestBody List<Session> pWorkouts) {
		return super.removeWorkouts(pWorkouts);
	}

	@RequestMapping(value = "/retrieveCal", produces = "text/plain")
	public @ResponseBody String retrieveCalendar() {

		return "Hello";
	}

	WorkoutDao<Session> getDao() {
		return mSessionDao;
	}

}
