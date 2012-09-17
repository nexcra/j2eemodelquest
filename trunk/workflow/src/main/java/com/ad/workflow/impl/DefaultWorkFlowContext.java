package com.ad.workflow.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.model.WorkFlow;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultWorkFlowContext implements IWorkFlowContext {

	private DefaultWorkFlowContext() {
	}

	private static volatile DefaultWorkFlowContext instance;

	public static DefaultWorkFlowContext getInstance() {
		if (instance == null)// 1
			synchronized (DefaultWorkFlowContext.class) {// 2
				if (instance == null)// 3
					instance = new DefaultWorkFlowContext();
			}
		return instance;
	}

	@Override
	public WorkFlow getWorkFlow(Connection conn, Integer wfid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from workflow where id=?", new BeanHandler<WorkFlow>(WorkFlow.class), new Object[] { wfid });
	}

	@Override
	public List<WorkFlow> getWorkFlows(Connection conn) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from workflow order by id", new BeanListHandler<WorkFlow>(WorkFlow.class));
	}

	@Override
	public List<WorkFlowNode> getWorkFlowNodes(Connection conn, Integer wfid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_NODE where wfid=? order by id", new BeanListHandler<WorkFlowNode>(WorkFlowNode.class), new Object[] { wfid });
	}

	@Override
	public WorkFlowNode getWorkFlowNode(Connection conn, Integer nid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_NODE where id=?", new BeanHandler<WorkFlowNode>(WorkFlowNode.class), new Object[] { nid });
	}

	@Override
	public List<WorkFlowTransition> getWorkFlowTransitions(Connection conn, Integer wfid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_TRANSITION where wfid=? order by id", new BeanListHandler<WorkFlowTransition>(WorkFlowTransition.class), new Object[] { wfid });
	}

	@Override
	public WorkFlowTransition getWorkFlowTransition(Connection conn, Integer tid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_TRANSITION where id=?", new BeanHandler<WorkFlowTransition>(WorkFlowTransition.class), new Object[] { tid });
	}

	@Override
	public VWorkFlowDocument getVWorkFlowDocument(Connection conn, Integer did) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_DOCUMENT where id=?", new BeanHandler<VWorkFlowDocument>(VWorkFlowDocument.class), new Object[] { did });

	}

	@Override
	public WorkFlowDocumentStep getWorkFlowDocumentStep(Connection conn, Integer sid) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(conn, "select * from WORKFLOW_DOCUMENT_STEPS where id=?", new BeanHandler<WorkFlowDocumentStep>(WorkFlowDocumentStep.class), new Object[] { sid });

	}

}
