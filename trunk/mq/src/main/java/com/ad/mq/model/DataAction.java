package com.ad.mq.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 事件行为 [table:MQ$ACTION]
 * 
 * @author YMQ
 * 
 */

@Entity(name = "MQ$ACTION")
public class DataAction implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String name;
	private String cfg;
	private String outType;
	private String service;
	private Boolean status;
	private String rmk;

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

	public String getCfg() {
		return cfg;
	}

	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

	public String getOutType() {
		return outType;
	}

	public void setOutType(String outType) {
		this.outType = outType;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

}
