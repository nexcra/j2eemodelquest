package com.ad.workflow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ad.workflow.model.WorkFlow;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * WorkFlow上下文
 * 
 * @author YMQ
 * 
 */
public interface IWorkFlowContext {


	/**
	 * 得到流程实例
	 * 
	 * @param conn
	 * @param wfid
	 * @return
	 * @throws SQLException
	 */
	WorkFlow getWorkFlow(Connection conn, Integer wfid) throws SQLException;

	/**
	 * 得到所有流程实例
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	List<WorkFlow> getWorkFlows(Connection conn) throws SQLException;

	/**
	 * 得到所有节点
	 * 
	 * @param conn
	 * @param wfid
	 * @return
	 * @throws SQLException
	 */
	List<WorkFlowNode> getWorkFlowNodes(Connection conn, Integer wfid) throws SQLException;

	/**
	 * 得到指定的节点
	 * 
	 * @param conn
	 * @param nid
	 * @return
	 * @throws SQLException
	 */
	WorkFlowNode getWorkFlowNode(Connection conn, Integer nid) throws SQLException;

	/**
	 * 得到所有扭转策略
	 * 
	 * @param conn
	 * @param wfid
	 * @return
	 * @throws SQLException
	 */
	List<WorkFlowTransition> getWorkFlowTransitions(Connection conn, Integer wfid) throws SQLException;

	/**
	 * 得到指定的扭转策略
	 * 
	 * @param conn
	 * @param tid
	 * @return
	 */
	WorkFlowTransition getWorkFlowTransition(Connection conn, Integer tid) throws SQLException;
	
	/**
	 * 获取指定的document
	 * @param conn
	 * @param did
	 * @return
	 * @throws SQLException
	 */
	VWorkFlowDocument getVWorkFlowDocument(Connection conn ,Integer did) throws SQLException;
	
	/**
	 * 获取当前人员的步骤数据
	 * @param conn
	 * @param did
	 * @param nid
	 * @param usr
	 * @return
	 * @throws SQLException
	 */
	WorkFlowDocumentStep getWorkFlowDocumentStep(Connection conn ,Integer sid) throws SQLException;

}
