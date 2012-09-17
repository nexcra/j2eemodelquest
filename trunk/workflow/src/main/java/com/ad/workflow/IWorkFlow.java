package com.ad.workflow;

import java.sql.Connection;
import java.util.List;

import com.ad.mq.model.IUser;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * 常用参数
 * 
 * @author YMQ
 * 
 */
public interface IWorkFlow {
	int NODE_START = 0; // 开始节点
	int NODE_NODE = 1; // 一般节点【不停留，自动向下】
	int NODE_TASKNODE = 2; // 人工节点【只对应一个审批人WorkFlowNode.usrid】
	int NODE_TASKNODEPOOL = 3; // 人工节点【对应多个审批人，来自于WorkFlowNodePool ,推与拉二种方式】
	int NODE_DECISION = 4; // 条件选择扭转节点
	int NODE_FORK = 5; // fork
	int NODE_JOIN = 6;// join
	int NODE_END = 9; // 结束节点
	
	int STEP_WORKING = 0;
	int STEP_SUBMIT  =1;
	int STEP_BACK = -1;
	int STEP_SUSPEND = 4;

	VWorkFlowDocument Start(Connection conn, Integer wfid, IUser usrid) throws Exception;

	/**
	 * 获取指定节点的transitions
	 * @param conn
	 * @param nid
	 * @return
	 * @throws Exception
	 */
	List<WorkFlowTransition> getTransitionsByNode(Connection conn, Integer nid) throws Exception;
	
	/**
	 * 获取指定节点的transitions
	 * @param conn
	 * @param did
	 * @return
	 * @throws Exception
	 */
	List<WorkFlowTransition> getTransitionsByDocument(Connection conn, Integer did) throws Exception;
	
	
	
}
