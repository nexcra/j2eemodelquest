package com.ad.mq.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 数据源 [table:MQ$OBJECT]
 * 
 * @author YMQ
 * 
 */
@Entity(name = "MQ$OBJECT")
public class DataObject implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String name;
	private String sql;
	private String beanname;
	private String rmk;
	private String cfg;
	private Integer token;
	@Transient
	private Integer auth = 0;
	
	
	
	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

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

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getBeanname() {
		return beanname;
	}

	public void setBeanname(String beanname) {
		this.beanname = beanname;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getCfg() {
		return cfg;
	}

	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

}
