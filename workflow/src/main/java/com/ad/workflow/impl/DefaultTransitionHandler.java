package com.ad.workflow.impl;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.IUser;
import com.ad.workflow.INodeHandler;
import com.ad.workflow.ITransitionHandler;
import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultTransitionHandler implements ITransitionHandler, DataBaseAware {
	private Logger log = Logger.getLogger(DefaultTransitionHandler.class);
	private DBControl db;
	private IWorkFlowContext cxt = DefaultWorkFlowContext.getInstance();

	/**
	 * 离开上一Node
	 * 
	 * @param conn
	 * @param document
	 * @param transition
	 * @param usr
	 * @throws Exception
	 */
	protected void submitNode(Connection conn, VWorkFlowDocument document, Integer fromnid, Integer tonid, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enterToNode invoke");
		}

		WorkFlowNode fromnode = cxt.getWorkFlowNode(conn, fromnid);
		Object nodeHandler = Class.forName(fromnode.getHandler()).newInstance();
		if (nodeHandler instanceof DataBaseAware) {
			((DataBaseAware) nodeHandler).setDBControl(this.db);
		}
		if (nodeHandler instanceof INodeHandler) {
			((INodeHandler) nodeHandler).beforeSubmit(fromnode, tonid, document, conn, sid, usrid);
			((INodeHandler) nodeHandler).submit(fromnode, tonid, document, conn, sid, usrid);
		} else {
			throw new Exception(fromnode.getHandler() + "不是INodeHandler接口的实现类！");
		}
	}

	/**
	 * 进入下一Node
	 * 
	 * @param conn
	 * @param document
	 * @param transition
	 * @param usr
	 * @throws Exception
	 */
	protected void enterNode(Connection conn, VWorkFlowDocument document, Integer fromnid, Integer tonid, Integer sid,Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enterToNode invoke");
		}
		WorkFlowNode tonode = cxt.getWorkFlowNode(conn, tonid);
		if (fromnid > tonid){
			tonode.setUsrid(usrid);
		}
		Object nodeHandler = Class.forName(tonode.getHandler()).newInstance();
		if (nodeHandler instanceof DataBaseAware) {
			((DataBaseAware) nodeHandler).setDBControl(this.db);
		}
		if (nodeHandler instanceof INodeHandler) {
			((INodeHandler) nodeHandler).beforeEnter(fromnid, tonode, document, conn, sid,usrid);
			((INodeHandler) nodeHandler).enter(fromnid, tonode, document, conn, sid,usrid);
		} else {
			throw new Exception(tonode.getHandler() + "不是INodeHandler接口的实现类！");
		}
	}

	/**
	 * 回退
	 * @param conn
	 * @param document
	 * @param fromnid
	 * @param tonid
	 * @param sid
	 * @param usr
	 * @throws Exception
	 */
	protected void backNode(Connection conn, VWorkFlowDocument document, Integer fromnid, Integer tonid, Integer sid, Integer usrid,String msg) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enterToNode invoke");
		}
		WorkFlowNode node = cxt.getWorkFlowNode(conn, fromnid);
		Object nodeHandler = Class.forName(node.getHandler()).newInstance();
		if (nodeHandler instanceof DataBaseAware) {
			((DataBaseAware) nodeHandler).setDBControl(this.db);
		}
		if (nodeHandler instanceof INodeHandler) {
			((INodeHandler) nodeHandler).beforeBack(node, tonid, document, conn, sid, usrid ,msg);
			((INodeHandler) nodeHandler).back(node, tonid, document, conn, sid, usrid ,msg);
		} else {
			throw new Exception(node.getHandler() + "不是INodeHandler接口的实现类！");
		}
	}

	@Override
	public void transGo(Connection conn, VWorkFlowDocument document, WorkFlowTransition transition, Integer sid, Integer usrid) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("trans invoke");
		}

		submitNode(conn, document, transition.getFromnode(), transition.getTonode(), sid, usrid);
		enterNode(conn, document, transition.getFromnode(), transition.getTonode(), sid, usrid);
	}

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@Override
	public void transBack(Connection conn, VWorkFlowDocument document, WorkFlowTransition transition, Integer sid, Integer usrid ,String msg) throws Exception {
		backNode(conn, document, transition.getTonode(), transition.getFromnode(), sid, transition.getUsrid() ,msg);
		enterNode(conn, document, transition.getTonode(), transition.getFromnode(), sid, transition.getUsrid());

	}

}
