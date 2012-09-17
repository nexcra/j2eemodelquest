package com.ad.mq.model.view;

import javax.persistence.Transient;

import com.ad.mq.model.DataElement;

public class VDataElement extends DataElement {
	private static final long serialVersionUID = 1L;
	@Transient
	private Integer auth;

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

}
