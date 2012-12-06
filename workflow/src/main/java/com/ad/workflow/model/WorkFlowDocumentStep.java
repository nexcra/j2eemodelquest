package com.ad.workflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 工作流安排步骤
 * 
 * @author YMQ
 * 
 */
@Entity(name = "WORKFLOW$DOCUMENT$STEPS")
public class WorkFlowDocumentStep implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_WORKFLOW_DOCUMENT_STEPS_ID")
	protected Integer id;
	protected Integer did;
	protected Integer nid;
	protected Integer usrid;
	protected Timestamp enterdate;
	protected Timestamp submitdate;
	protected Timestamp backdate;
	protected String msg;
	protected Integer status;// 0,解挂,1,挂起

	protected Integer fromnid;
	protected Integer fromsid;


	public Integer getFromsid() {
		return fromsid;
	}

	public void setFromsid(Integer fromsid) {
		this.fromsid = fromsid;
	}

	public Integer getFromnid() {
		return fromnid;
	}

	public void setFromnid(Integer fromnid) {
		this.fromnid = fromnid;
	}

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

	public Integer getDid() {
		return did;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public Integer getUsrid() {
		return usrid;
	}

	public void setUsrid(Integer usrid) {
		this.usrid = usrid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getEnterdate() {
		return enterdate;
	}

	public void setEnterdate(Timestamp enterdate) {
		this.enterdate = enterdate;
	}

	public Timestamp getSubmitdate() {
		return submitdate;
	}

	public void setSubmitdate(Timestamp submitdate) {
		this.submitdate = submitdate;
	}

	public Timestamp getBackdate() {
		return backdate;
	}

	public void setBackdate(Timestamp backdate) {
		this.backdate = backdate;
	}

}
