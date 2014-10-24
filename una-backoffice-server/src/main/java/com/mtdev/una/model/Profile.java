package com.mtdev.una.model;

import java.util.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mtdev.una.data.view.Views;
import com.mtdev.una.tools.JsonDateSerializer;

@Data
@Document(collection = "profiles")
public class Profile {

	@Id
	private String id;

	@Field
	@JsonView(Views.Public.class)
	Integer state;

	@Field
	@JsonView(Views.Public.class)
	Integer invalid;

	@Field
	@JsonView(Views.Public.class)
	String username;

	@Field
	@JsonView(Views.Public.class)
	String category;
	

	@Field
	@JsonView(Views.Public.class)
	String regCategory;
	
	@Field
	@JsonView(Views.Public.class)
	String level;
	
	@Field
	@JsonView(Views.Public.class)
	String autonomous;

	@Field
	@JsonView(Views.Public.class)
	String name;

	@Field
	@JsonView(Views.Public.class)
	String surname;

	@Field
	@JsonView(Views.Public.class)
	String sex;

	@Field
	@JsonView(Views.Public.class)
	String licence;

	@Field
	@JsonView(Views.Public.class)
	Date birthdate;

	@Field
	@JsonView(Views.ExtendedPublic.class)
	String address;

	@Field
	@JsonView(Views.ExtendedPublic.class)
	String zipcode;

	@Field
	@JsonView(Views.ExtendedPublic.class)
	String city;

	@Field
	@JsonView(Views.ExtendedPublic.class)
	String telephone;

	@Field
	@JsonView(Views.ExtendedPublic.class)
	String nationality;

	@Field
	@JsonView(Views.Internal.class)
	Object data;

	@Field
	@JsonView(Views.Public.class)
	@JsonSerialize(using=JsonDateSerializer.class)
	Date insertDate;

	public Profile() {

	};

	public Profile(String pUsername) {
		username = pUsername;
	}

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String pUsername) {
		username = pUsername;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String pCategory) {
		category = pCategory;
	}
	
	public String getRegCategory() {
		return regCategory;
	}

	public void setRegCategory(String pRegCategory) {
		regCategory = pRegCategory;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer pState) {
		state = pState;
	}
	
	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer pInvalid) {
		invalid = pInvalid;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String pLevel) {
		level = pLevel;
	}

	public String getAutonomous() {
		return autonomous;
	}

	public void setAutonomous(String pAutonomous) {
		autonomous = pAutonomous;
	}

	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = pName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String pSurname) {
		surname = pSurname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String pSex) {
		sex = pSex;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String pLicence) {
		licence = pLicence;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date pBirthdate) {
		birthdate = pBirthdate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String pAddress) {
		address = pAddress;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String pZipcode) {
		zipcode = pZipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String pCity) {
		city = pCity;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String pTelephone) {
		telephone = pTelephone;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String pNationality) {
		nationality = pNationality;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object pData) {
		data = pData;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date pInsertDate) {
		insertDate = pInsertDate;
	}

}
