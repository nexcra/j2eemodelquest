package com.ad.report.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "REPORT$SRC_CLMN")
public class ReportSrcClmn implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private Integer srcid;
	private String clmnname;
	private String labelname;
	private Integer datatype;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSrcid() {
		return srcid;
	}

	public void setSrcid(Integer srcid) {
		this.srcid = srcid;
	}

	public String getClmnname() {
		return clmnname;
	}

	public void setClmnname(String clmnname) {
		this.clmnname = clmnname;
	}

	public String getLabelname() {
		return labelname;
	}

	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	public Integer getDatatype() {
		return datatype;
	}

	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
	}

}
