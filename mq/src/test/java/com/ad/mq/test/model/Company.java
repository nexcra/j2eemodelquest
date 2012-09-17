package com.ad.mq.test.model;

import java.util.List;

public class Company {
	private String name;
	private List<Person> persons;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Person> getPersons() {
		return persons;
	}
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

}
