package com.ad.workflow.impl;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.ad.mq.model.IUser;
import com.ad.workflow.IWorkFlow;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * 默认开始节点行为
 * @author YMQ
 *
 */
public class DefaultStartNodeHandler extends NodeHandlerAdapter {
	private Logger log = Logger.getLogger(DefaultStartNodeHandler.class);
	@Override
	public void enter(Integer fromnode,WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enter invoke!");
		}
		//this.db.update(conn, "update WORKFLOW_DOCUMENT set usrid = ? where id =?", new Object[] { usr.getUserId(), node.getId(), document.getId() });
		WorkFlowDocumentStep step = new WorkFlowDocumentStep();
		step.setDid(document.getId());
		step.setEnterdate(new Timestamp(System.currentTimeMillis()));
		step.setNid(node.getId());
		step.setUsrid(document.getUsrid());
		step.setStatus(IWorkFlow.STEP_WORKING);
		step.setFromnid(fromnode);
		step.setFromsid(sid);
		this.db.insert(conn, step);
	}
}
