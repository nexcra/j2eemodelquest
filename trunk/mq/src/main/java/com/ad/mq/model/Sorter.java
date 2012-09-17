package com.ad.mq.model;

import java.io.Serializable;

/**
 * Sorter来自Extjs.sort
 * 
 * @author YMQ
 * 
 */
public class Sorter implements Serializable {
	private static final long serialVersionUID = 1L;
	private String property;
	private String direction;

	/**
	 * 字段名称
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * ASC | DESC
	 * @return
	 */
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
