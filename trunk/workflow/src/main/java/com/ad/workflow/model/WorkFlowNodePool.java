package com.ad.workflow.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 工作流节点审批人员
 * @author YMQ
 *
 */
@Entity(name = "WORKFLOW_NODE_POOL")
public class WorkFlowNodePool implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer nid;
	@Id
	private Integer usrid;

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

}
