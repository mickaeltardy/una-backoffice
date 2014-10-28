package com.mtdev.una.business;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.mtdev.una.model.WorkoutImpl;
import com.mtdev.una.tools.FileTools;

@Component
public class WorkoutsManager {

	public Object mWorkoutsData = null;

	public WorkoutsManager() {
		String lWorkoutsData = FileTools
				.readFile("/datasource/workoutsData.json");

		Gson lGson = new Gson();

		mWorkoutsData = lGson.fromJson(lWorkoutsData, Object.class);
	}

	public String getWorkoutLabel(WorkoutImpl pWorkout) {
		StringBuffer lOutput = new StringBuffer();
		if (!StringUtils.isEmpty(pWorkout.getCategory())) {
			Map<Object, Object> lMap = (Map<Object, Object>) mWorkoutsData;

			List<Object> lClasses = (List<Object>) lMap.get("workoutClasses");
			for (Object lClass : lClasses) {
				Map<Object, Object> lClassObj = (Map<Object, Object>) lClass;
				if (((String) lClassObj.get("code")).compareTo(pWorkout
						.getCategory()) == 0) {
					lOutput.append((String) lClassObj.get("label") + " " );
				}
			}
		}
		if (!StringUtils.isEmpty(pWorkout.getType())) {
			lOutput.append(pWorkout.getType() + "/");
		}
		if (!StringUtils.isEmpty(pWorkout.getBoat())) {
			lOutput.append(pWorkout.getBoat() + "/");
		}
		if (!StringUtils.isEmpty(pWorkout.getDistance())) {
			lOutput.append(pWorkout.getDistance() + "km/");
		}
		if (!StringUtils.isEmpty(pWorkout.getDuration())) {
			lOutput.append(pWorkout.getDuration() + "/");
		}
		if (!StringUtils.isEmpty(pWorkout.getLaps())) {
			lOutput.append(pWorkout.getLaps() + " tours");
		}
		if (lOutput.length() == 0) {
			lOutput.append(pWorkout.getDescription().replaceAll(",", "\\,"));
		}

		return lOutput.toString();
	}

}
