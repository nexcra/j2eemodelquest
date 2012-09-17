package com.ad.workflow.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 工作流节点
 * 
 * @author YMQ
 * 
 */
@Entity(name = "WORKFLOW_NODE")
public class WorkFlowNode implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_WORKFLOW")
	private Integer id;
	private Integer wfid;
	private String name;
	private Integer type;
	private String handler;
	private String cfg;
	private Double worktime;
	private Integer worktimeType;
	private Integer usrid;

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWfid() {
		return wfid;
	}

	public void setWfid(Integer wfid) {
		this.wfid = wfid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCfg() {
		return cfg;
	}

	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

	public Double getWorktime() {
		return worktime;
	}

	public void setWorktime(Double worktime) {
		this.worktime = worktime;
	}

	public Integer getWorktimeType() {
		return worktimeType;
	}

	public void setWorktimeType(Integer worktimeType) {
		this.worktimeType = worktimeType;
	}

	public Integer getUsrid() {
		return usrid;
	}

	public void setUsrid(Integer usrid) {
		this.usrid = usrid;
	}

}
