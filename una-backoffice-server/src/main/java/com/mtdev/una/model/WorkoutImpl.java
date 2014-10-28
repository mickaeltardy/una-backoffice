package com.mtdev.una.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mtdev.una.tools.JsonDateDeserializer;
import com.mtdev.una.tools.JsonDateSerializer;

public class WorkoutImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -275825226591791177L;

	@Id
	@JsonProperty
	private String id;

	@Field
	protected String authorId;

	@Field
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	protected Date date;

	@Field
	protected Object athleteId;

	@Field
	protected String description;

	@Field
	protected String comment;

	@Field
	protected Object distance;

	@Field
	protected Object duration;

	@Field
	protected Object laps;

	@Field
	protected String type;

	@Field
	protected String category;

	@Field
	protected String boat;

	@Field
	protected Object athleteLevel;

	@Field
	protected Object athleteCategory;

	@Field
	protected Object athleteSex;

	@Field
	protected int itemType;

	
	
	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String pAuthorId) {
		authorId = pAuthorId;
	}

	public Object getAthleteId() {
		return athleteId;
	}

	public void setAthleteId(Object pAthleteId) {
		athleteId = pAthleteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String pDescription) {
		description = pDescription;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String pComment) {
		comment = pComment;
	}

	public Object getDistance() {
		return distance;
	}

	public void setDistance(Object pDistance) {
		distance = pDistance;
	}

	public Object getDuration() {
		return duration;
	}

	public void setDuration(Object pDuration) {
		duration = pDuration;
	}

	public Object getLaps() {
		return laps;
	}

	public void setLaps(Object pLaps) {
		laps = pLaps;
	}

	public String getType() {
		return type;
	}

	public void setType(String pType) {
		type = pType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String pCategory) {
		category = pCategory;
	}

	public String getBoat() {
		return boat;
	}

	public void setBoat(String pBoat) {
		boat = pBoat;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date pDate) {
		date = pDate;
	}

	public Object getAthleteLevel() {
		return athleteLevel;
	}

	public void setAthleteLevel(Object pAthleteLevel) {
		athleteLevel = pAthleteLevel;
	}

	public Object getAthleteCategory() {
		return athleteCategory;
	}

	public void setAthleteCategory(Object pAthleteCategory) {
		athleteCategory = pAthleteCategory;
	}

	public Object getAthleteSex() {
		return athleteSex;
	}

	public void setAthleteSex(Object pAthleteSex) {
		athleteSex = pAthleteSex;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int pItemType) {
		itemType = pItemType;
	}

	
}
