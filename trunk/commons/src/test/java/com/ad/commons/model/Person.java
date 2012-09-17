package com.ad.commons.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity(name="Test_Person")
public class Person {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="s_gen")
	@SequenceGenerator(name = "s_gen",sequenceName="SEQ_T_PERSON")
	private Integer id;
	private String name;
	@Transient
	private String sex;
	@Column(name="e_mail")
	private String email;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
