package com.ad.workflow.impl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.IUser;
import com.ad.workflow.INodeHandler;
import com.ad.workflow.IWorkFlow;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;

public abstract class NodeHandlerAdapter implements INodeHandler, DataBaseAware {
	private Logger log = Logger.getLogger(NodeHandlerAdapter.class);
	protected DBControl db;

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@Override
	public void beforeEnter(Integer  fromnode,WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeEnter invoke!");
		}
	};

	@Override
	public WorkFlowDocumentStep enter(Integer  fromnode,WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enter invoke!");
		}
		
		WorkFlowDocumentStep step = new WorkFlowDocumentStep();
		step.setDid(document.getId());
		step.setEnterdate(new Timestamp(System.currentTimeMillis()));
		step.setNid(node.getId());
		step.setUsrid(null);
		step.setStatus(IWorkFlow.STEP_WORKING);
		step.setFromnid(fromnode);
		step.setFromsid(sid);
		Map<String,Object> ids = this.db.insert(conn, step);
		step.setId(Integer.parseInt(ids.get("id").toString()));
		return step;
	};

	@Override
	public void beforeSubmit(WorkFlowNode node, Integer  tonid, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeSubmit invoke!");
		}
	};

	@Override
	public void submit(WorkFlowNode node, Integer  tonid, VWorkFlowDocument document, Connection conn, Integer sid ,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("submit invoke!");
		}
		this.db.update(conn, "update WORKFLOW_DOCUMENT_STEPS  set SUBMITDATE =? ,status = ? where id=?",
				new Object[] { new Timestamp(System.currentTimeMillis()) ,IWorkFlow.STEP_SUBMIT ,sid });
		// Object trans = Class.forName(transition.getExecute()).newInstance();
		// if (trans instanceof DataBaseAware) {
		// ((DataBaseAware) trans).setDBControl(this.db);
		// }
		// if (trans instanceof ITransitionHandler) {
		// ((ITransitionHandler) trans).trans(conn, document, transition, usr);
		// } else {
		// throw new Exception(transition.getExecute() +
		// "不是ITransitionHandler接口的实现类！");
		// }
	};

	@Override
	public void beforeBack(WorkFlowNode node,Integer  tonid, VWorkFlowDocument document, Connection conn, Integer sid,IUser usr,String msg) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeBack invoke!");
		}
	};

	@Override
	public void back(WorkFlowNode node,Integer  tonid,VWorkFlowDocument document, Connection conn, Integer sid,IUser usr,String msg) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("back invoke!");
		}
		this.db.update(conn, "update WORKFLOW_DOCUMENT_STEPS set status =?,backdate = ? ,msg=? where id = ?", new Object[]{ IWorkFlow.STEP_BACK  ,new Timestamp(System.currentTimeMillis())  ,msg ,sid});
//		WorkFlowNode node = (WorkFlowNode) this.db.query2Bean(conn, "select * from WORKFLOW_NODE where id=?", WorkFlowNode.class, new Object[] { transition.getFromnode() });
//		Object nodeHandler = Class.forName(node.getHandler()).newInstance();
//		if (nodeHandler instanceof DataBaseAware) {
//			((DataBaseAware) nodeHandler).setDBControl(this.db);
//		}
//		if (nodeHandler instanceof INodeHandler) {
//			((INodeHandler) nodeHandler).beforeEnter(transition,node, document, conn, usr);
//			((INodeHandler) nodeHandler).enter(transition,node, document, conn, usr);
//		} else {
//			throw new Exception(node.getHandler() + "不是INodeHandler的实现类！");
//		}
	};

	@Override
	public void beforeSuspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid,IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("beforeSuspend invoke!");
		}
	};

	@Override
	public void suspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn,Integer sid, IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("suspend invoke!");
		}
	};

}
