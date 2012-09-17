package com.ad.mq.model;

import java.io.Serializable;

/**
 * Filter来自Extjs.Filter
 * 
 * @author YMQ
 * 
 */
public class Filter implements Serializable {
	private static final long serialVersionUID = 1L;

	private String type;
	private String comparison;
	private String value;
	private String field;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComparison() {
		return comparison;
	}

	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
