package com.ad.report.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "REPORT$OBJ")
public class ReportObj implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_MQ_PUBLIC")
	private Integer id;
	private String name;
	private Integer srcid;
	private String cfg;
	private Integer usrid;

	private Boolean ispublic;
	
	public Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(Boolean ispublic) {
		this.ispublic = ispublic;
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

	public Integer getSrcid() {
		return srcid;
	}

	public void setSrcid(Integer srcid) {
		this.srcid = srcid;
	}

	public String getCfg() {
		return cfg;
	}

	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

	public Integer getUsrid() {
		return usrid;
	}

	public void setUsrid(Integer usrid) {
		this.usrid = usrid;
	}

}
