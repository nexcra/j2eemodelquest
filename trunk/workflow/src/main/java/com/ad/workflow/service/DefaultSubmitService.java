package com.ad.workflow.service;

import java.sql.Connection;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.IUser;
import com.ad.workflow.ITransitionHandler;
import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.impl.DefaultWorkFlowContext;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultSubmitService implements DataBaseAware {

	private DBControl db;

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	public void submit(Integer did, Integer tid ,Integer sid, IUser usr) throws Exception {
		IWorkFlowContext cxt = DefaultWorkFlowContext.getInstance();
		Connection conn = this.db.getDataSource().getConnection();
		WorkFlowTransition transition = cxt.getWorkFlowTransition(conn, tid);
		VWorkFlowDocument document = cxt.getVWorkFlowDocument(conn, did);
		Object trans = Class.forName(transition.getExecute()).newInstance();
		if (trans instanceof DataBaseAware){
			((DataBaseAware) trans).setDBControl(this.db);
		}
		if (trans instanceof ITransitionHandler){
			((ITransitionHandler) trans).transGo(conn, document, transition ,sid, usr);
		}else{
			throw new Exception(transition.getExecute() + "不是ITransitionHandler接口的实现类！");
		}
		
		
	}

}
