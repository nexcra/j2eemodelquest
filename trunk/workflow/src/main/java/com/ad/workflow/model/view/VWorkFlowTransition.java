package com.ad.workflow.model.view;

import com.ad.workflow.model.WorkFlowTransition;

public class VWorkFlowTransition extends WorkFlowTransition {
	private static final long serialVersionUID = 1L;
	protected String fromnodename;
	protected String tonodename;

	public String getFromnodename() {
		return fromnodename;
	}

	public void setFromnodename(String fromnodename) {
		this.fromnodename = fromnodename;
	}

	public String getTonodename() {
		return tonodename;
	}

	public void setTonodename(String tonodename) {
		this.tonodename = tonodename;
	}

}
