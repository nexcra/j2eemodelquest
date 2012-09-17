package com.ad.workflow.model.view;

import com.ad.workflow.model.WorkFlowDocumentStep;
/**
 * 流程视图
 * @author YMQ
 *
 */
public class VWorkFlowDocumentStep extends WorkFlowDocumentStep {
	private static final long serialVersionUID = 1L;

	protected String nodename;//节点名称
	protected String username;//用户名称
	protected String statusname;//状态名称

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

}
