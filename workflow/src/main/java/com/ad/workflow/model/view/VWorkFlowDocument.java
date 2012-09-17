package com.ad.workflow.model.view;

import java.sql.Timestamp;

import com.ad.workflow.model.WorkFlowDocument;

public class VWorkFlowDocument extends WorkFlowDocument {

	private static final long serialVersionUID = 1L;

	protected String usrname;
	protected String nodename;
	protected String wfname;
	protected Integer nid;
	protected Integer nodetype;
	protected Timestamp enterdate;
	protected Timestamp submitdate;
	protected Timestamp backdate;
	protected Integer stepstatus;
	protected String msg;
	protected Integer stepid;

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
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

	public Integer getStepstatus() {
		return stepstatus;
	}

	public void setStepstatus(Integer stepstatus) {
		this.stepstatus = stepstatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getStepid() {
		return stepid;
	}

	public void setStepid(Integer stepid) {
		this.stepid = stepid;
	}

	public Integer getNodetype() {
		return nodetype;
	}

	public void setNodetype(Integer nodetype) {
		this.nodetype = nodetype;
	}

	public String getUsrname() {
		return usrname;
	}

	public void setUsrname(String usrname) {
		this.usrname = usrname;
	}

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public String getWfname() {
		return wfname;
	}

	public void setWfname(String wfname) {
		this.wfname = wfname;
	}

}
