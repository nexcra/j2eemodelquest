package com.ad.mq.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 元素权限[table:MQ$ELEMENT_AUTH]
 * 
 * @author YMQ
 * 
 */
@Entity(name = "MQ$ELEMENT_AUTH")
public class DataElementAuth implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer elementid;
	@Id
	private Integer userid;
	private Integer auth;

	public Integer getElementid() {
		return elementid;
	}

	public void setElementid(Integer elementid) {
		this.elementid = elementid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

}
