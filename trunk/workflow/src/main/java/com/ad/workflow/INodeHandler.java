package com.ad.workflow;

import java.sql.Connection;

import com.ad.mq.model.IUser;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * 节点事件
 * 
 * @author YMQ
 * 
 */
public interface INodeHandler {

	/**
	 * 进入审批前
	 * @param transition 
	 * 
	 * @param node
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void beforeEnter(Integer  fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn,Integer sid , Integer usrId) throws Exception;

	/**
	 * 进入审批
	 * @param transition 
	 * 
	 * @param node
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	WorkFlowDocumentStep enter(Integer  fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid ,Integer usrId) throws Exception;

	/**
	 * 提交之前
	 * 
	 * @param transition
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void beforeSubmit(WorkFlowNode node, Integer  tonid, VWorkFlowDocument document, Connection conn, Integer sid ,Integer usrId) throws Exception;

	/**
	 * 提交
	 * 
	 * @param transition
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void submit(WorkFlowNode node,Integer  tonid,  VWorkFlowDocument document, Connection conn, Integer sid ,Integer usrId) throws Exception;

	/**
	 * 回退之前
	 * 
	 * @param transition
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void beforeBack(WorkFlowNode node,Integer  tonid, VWorkFlowDocument document, Connection conn,Integer sid, Integer usrId,String msg) throws Exception;

	/**
	 * 回退
	 * 
	 * @param transition
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void back(WorkFlowNode node,Integer  tonid, VWorkFlowDocument document, Connection conn,Integer sid, Integer usrId,String msg) throws Exception;

	/**
	 * 挂起之前
	 * 
	 * @param node
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void beforeSuspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid,Integer usrId) throws Exception;

	/**
	 * 挂起
	 * 
	 * @param node
	 * @param document
	 * @param conn
	 * @param usr
	 * @throws Exception
	 */
	void suspend(WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid,Integer usrId) throws Exception;
}
