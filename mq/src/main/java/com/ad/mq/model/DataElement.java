package com.ad.mq.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 数据源元素说明 [table:MQ$ELEMENT]
 * 
 * @author YMQ
 * 
 */
@Entity(name = "MQ$ELEMENT")
public class DataElement implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private Integer parentid;
	private String fieldname;
	private String fieldvalue;
	private String ftype;
	private String ftypeattrs;
	private String columnattrs;
	private String vtype;
	private String vtypeattrs;
	private String xtype;
	private String xtypeattrs;
	private String rmk;
	private Integer gridindex;
	private Integer formindex;
	private Boolean gridshow;
	private Boolean formshow;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getFtypeattrs() {
		return ftypeattrs;
	}

	public void setFtypeattrs(String ftypeattrs) {
		this.ftypeattrs = ftypeattrs;
	}

	public String getColumnattrs() {
		return columnattrs;
	}

	public void setColumnattrs(String columnattrs) {
		this.columnattrs = columnattrs;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getVtypeattrs() {
		return vtypeattrs;
	}

	public void setVtypeattrs(String vtypeattrs) {
		this.vtypeattrs = vtypeattrs;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public String getXtypeattrs() {
		return xtypeattrs;
	}

	public void setXtypeattrs(String xtypeattrs) {
		this.xtypeattrs = xtypeattrs;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Integer getGridindex() {
		return gridindex;
	}

	public void setGridindex(Integer gridindex) {
		this.gridindex = gridindex;
	}

	public Integer getFormindex() {
		return formindex;
	}

	public void setFormindex(Integer formindex) {
		this.formindex = formindex;
	}

	public Boolean getGridshow() {
		return gridshow;
	}

	public void setGridshow(Boolean gridshow) {
		this.gridshow = gridshow;
	}

	public Boolean getFormshow() {
		return formshow;
	}

	public void setFormshow(Boolean formshow) {
		this.formshow = formshow;
	}

}
