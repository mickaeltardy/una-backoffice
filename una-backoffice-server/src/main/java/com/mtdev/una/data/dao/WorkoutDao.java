package com.mtdev.una.data.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class WorkoutDao<T> {

	@Autowired
	MongoOperations mMongoOperations;

	public List<T> getAllWorkouts(Class<T> pClass) {
		List<T> lSessions = mMongoOperations.findAll(pClass);
		return lSessions;
	}

	public List<T> getWorkoutsByRequest(Map<Object, Object> pRequest,
			Class<T> pClass) {

		Query lQuery = getQueryFromMap(pRequest);

		List<T> lSessions = mMongoOperations.find(lQuery, pClass);
		return lSessions;

	}

	public boolean saveWorkout(T pWorkout) {

		boolean lResult = false;
		if (pWorkout != null) {
			try {
				mMongoOperations.save(pWorkout);
				return true;
			} catch (Exception lE) {

			}
		}

		return lResult;

	}
	

	public boolean removeWorkout(T pWorkout) {

		boolean lResult = false;
		if (pWorkout != null) {
			try {
				mMongoOperations.remove(pWorkout);
				return true;
			} catch (Exception lE) {

			}
		}

		return lResult;

	}

	protected Query getQueryFromMap(Map<Object, Object> pRequest) {
		Query lQuery = new Query();

		if (pRequest != null && pRequest.size() > 0) {
			for (Entry<Object, Object> lRequestItem : pRequest.entrySet()) {

				Criteria lCriteria = getCustomCriteria(lRequestItem);
				lQuery.addCriteria(lCriteria);

			}
		}

		return lQuery;
	}

	protected Criteria getCustomCriteria(Entry<Object, Object> pRequestItem) {
		String lKey = (String) pRequestItem.getKey();

		Object lValue = pRequestItem.getValue();
		Criteria lCriteria = null;
		if (lKey.compareTo("dateFrom") == 0) {
			lCriteria = Criteria.where("date").gte(lValue);
		} else if (lKey.compareTo("dateTo") == 0) {
			lCriteria = Criteria.where("date").lte(lValue);
		} else {
			lCriteria = Criteria.where(lKey).is(lValue);
		}
		return lCriteria;
	}

	protected Object getTypedValue(Entry<Object, Object> pRequestItem) {

		return null;
	}

}
