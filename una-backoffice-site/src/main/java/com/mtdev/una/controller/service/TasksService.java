package com.mtdev.una.controller.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.data.dao.TaskDao;
import com.mtdev.una.data.dao.WorkoutDao;
import com.mtdev.una.model.Task;

@Controller
@RequestMapping("/tasks")
public class TasksService extends WorkoutService<Task> {

	@Autowired
	protected TaskDao mTaskDao;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object createNewWorkout(@RequestBody List<Task> pTasks) {

		return super.createNewWorkout(pTasks);

	}

	@RequestMapping(value = "/retrieve", method = RequestMethod.POST)
	public @ResponseBody Object retrieveWorkouts(
			@RequestBody Map<Object, Object> pRequest) {

		return super.retrieveWorkouts(pRequest, Task.class);
	}
	
	@Override
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object removeWorkout(@RequestBody Task pWorkout) {
		return super.removeWorkout(pWorkout);
	}

	@RequestMapping(value = "/retrieveCal", produces = "text/plain")
	public @ResponseBody String retrieveCalendar() {

		return "Hello";
	}
	

	WorkoutDao<Task> getDao(){
		return mTaskDao;
	}

}
