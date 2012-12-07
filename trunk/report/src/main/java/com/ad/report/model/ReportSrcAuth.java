package com.ad.report.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "REPORT$SRC_AUTH")
public class ReportSrcAuth implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer srcid;
	@Id
	private Integer usrid;

	public Integer getSrcid() {
		return srcid;
	}

	public void setSrcid(Integer srcid) {
		this.srcid = srcid;
	}

	public Integer getUsrid() {
		return usrid;
	}

	public void setUsrid(Integer usrid) {
		this.usrid = usrid;
	}

}
