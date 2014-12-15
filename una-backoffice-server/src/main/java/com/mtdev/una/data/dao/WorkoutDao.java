package com.mtdev.una.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class WorkoutDao<T> {

	@Autowired
	MongoOperations mMongoOperations;
	
	@Autowired
	MongoTemplate mMongoTemplate;

	public List<T> getAllWorkouts(Class<T> pClass) {
		List<T> lSessions = mMongoOperations.findAll(pClass);
		return lSessions;
	}

	public List<T> getWorkoutsByStrictRequest(Map<Object, Object> pRequest,
			Class<T> pClass) {

		Query lQuery = getQueryFromMap(pRequest, true);

		List<T> lSessions = mMongoOperations.find(lQuery, pClass);
		return lSessions;

	}

	public List<T> getWorkoutsByFlexRequest(Map<Object, Object> pRequest,
			Class<T> pClass) {

		Query lQuery = getQueryFromMap(pRequest, false);

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

	protected Query getQueryFromMap(Map<Object, Object> pRequest,
			boolean pStrict) {
		Query lQuery = new Query();

		if (pRequest != null && pRequest.size() > 0) {
			List<Criteria> lSpecificCriteria = getSpecificCriteria(pRequest,
					pStrict);
			/*
			
			*/
			Criteria lContentCriteria = null;
			List<Criteria> lContentCriteriaList = new ArrayList<>();

			Criteria lNullCriteria = null;
			List<Criteria> lNullCriteriaList = new ArrayList<>();

			for (Entry<Object, Object> lRequestItem : pRequest.entrySet()) {

				Criteria lCriteria = getCustomCriteria(lRequestItem, pStrict);
				/*
				 * if (lCriteria != null) { if (lContentCriteria == null)
				 * lContentCriteria = lCriteria; else
				 * lContentCriteria.andOperator(lCriteria); }
				 */
				if (lCriteria != null) {
					lContentCriteriaList.add(lCriteria);
				}
				/*
				if (!pStrict) {
					lRequestItem.setValue(null);
					Criteria lEmptyCriteria = getCustomCriteria(lRequestItem,
							true);
					if (lEmptyCriteria != null) {
						lNullCriteriaList.add(lEmptyCriteria);
					}
				}
				*/
			}
			/*
			 * if (lContentCriteria != null) { Criteria lFullCriteria = new
			 * Criteria() .orOperator(lContentCriteria);
			 * lQuery.addCriteria(lFullCriteria); }
			 */
			List<Criteria> lFullCriteriaList = new ArrayList<>();
			if (lContentCriteriaList.size() > 0) {
				lFullCriteriaList.add(new Criteria()
						.andOperator(lContentCriteriaList
								.toArray(new Criteria[0])));

			}
			if (lNullCriteriaList.size() > 0) {
				lFullCriteriaList
						.add(new Criteria().andOperator(lNullCriteriaList
								.toArray(new Criteria[0])));

			}
			if (lFullCriteriaList.size() > 0) {

				lSpecificCriteria
						.add(new Criteria().orOperator(lFullCriteriaList
								.toArray(new Criteria[0])));

			}

			// for (Criteria lCriteria : lSpecificCriteria) {
			lQuery.addCriteria(new Criteria().andOperator(lSpecificCriteria
					.toArray(new Criteria[0])));
			// }
		}

		return lQuery;
	}

	private List<Criteria> getSpecificCriteria(Map<Object, Object> pRequest,
			boolean pStrict) {
		List<Criteria> lCriteriaList = new ArrayList<Criteria>();

		if (pRequest.containsKey("dateFrom") && pRequest.containsKey("dateTo")) {
			lCriteriaList.add(new Criteria().andOperator(Criteria.where("date")
					.gte(pRequest.get("dateFrom")),
					Criteria.where("date").lte(pRequest.get("dateTo"))));
		} else if (pRequest.containsKey("dateFrom")) {
			lCriteriaList.add(Criteria.where("date").gte(
					pRequest.containsKey("dateFrom")));
		} else if (pRequest.containsKey("dateTo")) {
			lCriteriaList.add(Criteria.where("date").lte(
					pRequest.containsKey("dateTo")));
		}

		return lCriteriaList;
	}

	protected Criteria getCustomCriteria(Entry<Object, Object> pRequestItem,
			boolean pStrict) {
		String lKey = (String) pRequestItem.getKey();

		Object lValue = pRequestItem.getValue();
		Criteria lCriteria = null;
		if (lKey.compareTo("dateFrom") != 0 && lKey.compareTo("dateTo") != 0) {
			if (pStrict)
				lCriteria = Criteria.where(lKey).is(lValue);
			else
				lCriteria = new Criteria().orOperator(
						Criteria.where(lKey).is(null),
						Criteria.where(lKey).is(lValue));

		}
		return lCriteria;
	}

	protected Object getTypedValue(Entry<Object, Object> pRequestItem) {

		return null;
	}

}
