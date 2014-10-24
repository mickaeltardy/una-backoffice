package com.mtdev.una.controller.service;

import java.util.List;
import java.util.Map;

import com.mtdev.una.data.dao.WorkoutDao;

public abstract class WorkoutService<T> {

	public Object createNewWorkout(List<T> pWorkouts) {

		try {
			if (pWorkouts != null && pWorkouts.size() > 0) {
				for (T pWorkout : pWorkouts) {
					getDao().saveWorkout(pWorkout);
				}
			}

			return true;
		} catch (Exception lE) {
			lE.printStackTrace();
		}
		return null;

	}

	public Object removeWorkout(T pWorkout) {

		try {
			if (pWorkout != null) {
				getDao().removeWorkout(pWorkout);
			}

			return true;
		} catch (Exception lE) {
			lE.printStackTrace();
		}
		return null;

	}

	public Object retrieveWorkouts(Map<Object, Object> pRequest, Class<T> pClass) {

		return getDao().getWorkoutsByRequest(pRequest, pClass);
	}

	WorkoutDao<T> getDao() {
		return null;
	}

}
