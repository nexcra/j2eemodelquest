package com.ad.workflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 挂起与解挂记录
 * 
 * @author YMQ
 * 
 */
@Entity(name = "WORKFLOW$DOCUMENT$STEPS_SUSP")
public class WorkFlowDocumentStepSusp implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_WORKFLOW_DOCUMENT_STEPS_ID")
	private Integer id;
	private Integer sid;
	private Timestamp dodate;
	private Timestamp undate;
	private Integer status;
	private String msg;
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Timestamp getDodate() {
		return dodate;
	}
	public void setDodate(Timestamp dodate) {
		this.dodate = dodate;
	}
	public Timestamp getUndate() {
		return undate;
	}
	public void setUndate(Timestamp undate) {
		this.undate = undate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	
	
}
