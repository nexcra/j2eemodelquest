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
 * 默认结束节点行为
 * @author YMQ
 *
 */
public class DefaultEndNodeHandler extends NodeHandlerAdapter {
	private Logger log = Logger.getLogger(DefaultEndNodeHandler.class);

	@Override
	public WorkFlowDocumentStep enter(Integer  fromnode,WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enter invoke!");
		}
		this.db.update(conn, "update WORKFLOW$DOCUMENT set ENDTIME = ? where id =?", new Object[] { new Timestamp(System.currentTimeMillis()), document.getId() });
//		WorkFlowDocumentStep step = new WorkFlowDocumentStep();
//		step.setDid(document.getId());
//		step.setEnterdate(new Timestamp(System.currentTimeMillis()));
//		step.setNid(node.getId());
//		step.setUsrid(null);
//		step.setStatus(IWorkFlow.STEP_WORKING);
//		step.setFromnid(fromnode);
//		step.setFromsid(sid);
//		this.db.insert(conn, step);
		
		return super.enter(fromnode, node, document, conn, sid, usr);
	}


}
