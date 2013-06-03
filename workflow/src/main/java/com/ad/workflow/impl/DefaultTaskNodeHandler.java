package com.ad.workflow.impl;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ad.mq.model.IUser;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * TaskNode节点的缺省策略
 * 
 * @author YMQ
 * 
 */
public class DefaultTaskNodeHandler extends NodeHandlerAdapter {
	private Logger log = Logger.getLogger(DefaultTaskNodeHandler.class);

	@Override
	public void beforeEnter(Integer fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeEnter invoke!");
		}
	}

	@Override
	public WorkFlowDocumentStep enter(Integer fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid,Integer usrid ) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enter invoke!");
		}
		if (null == node.getUsrid()) {
			throw new Exception("TaskNode节点需要指定一个usrid");
		}
		//this.db.update(conn, "update WORKFLOW$DOCUMENT set usrid = ? ,nid = ? where id =?", new Object[] { node.getUsrid(), node.getId(), document.getId() });
		// WorkFlowDocumentStep step = new WorkFlowDocumentStep();
		// step.setDid(document.getId());
		// step.setEnterdate(new Timestamp(System.currentTimeMillis()));
		// step.setNid(node.getId());
		// step.setUsrid(node.getUsrid());
		// step.setStatus(IWorkFlow.STEP_WORKING);
		// step.setFromnid(fromnode);
		// step.setFromsid(sid);
		// this.db.insert(conn, step);
		return super.enter(fromnode, node, document, conn, sid, usrid);
	}

	@Override
	public void beforeSubmit(WorkFlowNode node, Integer tonid, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeSubmit invoke!");
		}
	}

	@Override
	public void submit(WorkFlowNode node, Integer tonid, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("submit invoke!");
		}
		super.submit(node, tonid, document, conn, sid, usrid);
	}

	@Override
	public void beforeBack(WorkFlowNode node, Integer tonid, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid, String msg) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeBack invoke!");
		}
	}

	@Override
	public void back(WorkFlowNode node, Integer tonid, VWorkFlowDocument document, Connection conn, Integer sid,Integer usrid, String msg) throws Exception {
		super.back(node, tonid, document, conn, sid, usrid, msg);
	}

	@Override
	public void beforeSuspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeSuspend invoke!");
		}
	}

	@Override
	public void suspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("suspend invoke!");
		}
	}

}
