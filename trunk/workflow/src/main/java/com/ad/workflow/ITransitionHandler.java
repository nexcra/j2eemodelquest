package com.ad.workflow;

import java.sql.Connection;

import com.ad.mq.model.IUser;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

/**
 * 扭转的push方式
 * @author YMQ
 *
 */
public interface ITransitionHandler {
	void transGo(Connection conn , VWorkFlowDocument document ,WorkFlowTransition transition,Integer sid ,IUser usr) throws Exception;
	void transBack(Connection conn , VWorkFlowDocument document ,WorkFlowTransition transition,Integer sid ,IUser usr,String msg) throws Exception;
}
