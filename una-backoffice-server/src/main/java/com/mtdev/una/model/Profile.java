package com.mtdev.una.model;

import java.util.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "profiles")
public class Profile {

	@Id
	private String id;

	@Field
	String username;

	@Field
	String category;

	@Field
	String name;

	@Field
	String surname;

	@Field
	String sex;

	@Field
	String licence;

	@Field
	Date birthdate;

	@Field
	String address;

	@Field
	String zipcode;

	@Field
	String city;

	@Field
	String telephone;

	@Field
	String nationality;

	@Field
	Object data;
	
	@Field
	Date insertDate;
	
	public Profile(){
		
	};
	public Profile(String pUsername){
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
