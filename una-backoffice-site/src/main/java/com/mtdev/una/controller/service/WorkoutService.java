package com.mtdev.una.controller.service;

import java.util.List;
import java.util.Map;

import com.mtdev.una.data.dao.WorkoutDao;
import com.mtdev.una.tools.Toolbox;

public abstract class WorkoutService<T> {

	public Object createNewWorkout(List<T> pWorkouts) {

		try {
			if (pWorkouts != null && pWorkouts.size() > 0) {
				for (T pWorkout : pWorkouts) {
					getDao().saveWorkout(pWorkout);
				}
			}

			return Toolbox.generateResult("status", "success");
		} catch (Exception lE) {
			lE.printStackTrace();
		}
		return Toolbox.generateResult("status", "failure", "error", "failedOperation");

	}

	public Object removeWorkouts(List<T> pWorkouts) {
		try {
			if (pWorkouts != null && pWorkouts.size() > 0) {
				for (T pWorkout : pWorkouts) {
					getDao().removeWorkout(pWorkout);
				}
			}
			return Toolbox.generateResult("status", "success");
		} catch (Exception lE) {
			lE.printStackTrace();
		}
		return Toolbox.generateResult("status", "failure", "error", "failedOperation");
	}

	public Object retrieveWorkouts(Map<Object, Object> pRequest, Class<T> pClass) {

		return getDao().getWorkoutsByFlexRequest(pRequest, pClass);
	}

	WorkoutDao<T> getDao() {
		return null;
	}

}
