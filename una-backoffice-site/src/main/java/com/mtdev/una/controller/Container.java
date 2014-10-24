package com.mtdev.una.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.mtdev.una.data.view.Views;

public class Container {

	@JsonView(Views.Public.class)
	private int id;

	@JsonView(Views.Public.class)
	private String name;

	@JsonView(Views.ExtendedPublic.class)
	private String value;

	@JsonView(Views.Internal.class)
	private List<String> elements;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getElements() {
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	public Container(int id, String name, String value, List<String> elements) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.elements = elements;
	}

}
