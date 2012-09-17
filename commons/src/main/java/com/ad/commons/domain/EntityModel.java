package com.ad.commons.domain;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 实体类描述
 * 
 * @author YMQ
 * 
 */
public class EntityModel {
	private Map<String, Id> pks = new Hashtable<String, Id>();
	private Map<String, Column> columns = new Hashtable<String, Column>();
	private Map<String, Field> fields = new Hashtable<String, Field>();

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}

	/**
	 * PK的类属性与表字段
	 * 
	 * @return
	 */
	public Map<String, Id> getPks() {
		return pks;
	}

	/**
	 * PK的类属性与表字段
	 * 
	 * @param pks
	 */
	public void setPks(Map<String, Id> pks) {
		this.pks = pks;
	}

	/**
	 * 非PK的类属性与表字段
	 * 
	 * @return
	 */
	public Map<String, Column> getColumns() {
		return this.columns;
	}

	/**
	 * 非PK的类属性与表字段
	 * 
	 * @param columns
	 */
	public void setColumns(Map<String, Column> columns) {
		this.columns = columns;
	}

}
