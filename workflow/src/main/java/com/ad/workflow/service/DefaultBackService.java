package com.ad.workflow.service;

import java.sql.Connection;
import java.util.List;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.IUser;
import com.ad.workflow.ITransitionHandler;
import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.impl.DefaultWorkFlowContext;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultBackService implements DataBaseAware {

	private DBControl db;
	private static String SQL = "select * from WORKFLOW$TRANSITION where tonode = ?";

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@SuppressWarnings("unchecked")
	public void back(Connection conn ,Integer did ,Integer nid ,Integer sid, IUser usr,String msg) throws Exception {
		IWorkFlowContext cxt = DefaultWorkFlowContext.getInstance();
//		Connection conn = this.db.getDataSource().getConnection();
		VWorkFlowDocument document = cxt.getVWorkFlowDocument(conn, did);
		List<WorkFlowTransition> transitions = (List<WorkFlowTransition>) this.db.query2BeanList(SQL, WorkFlowTransition.class, new Object[]{nid});
		Object transHdl;
		for(WorkFlowTransition trans : transitions){
			transHdl = Class.forName(trans.getExecute()).newInstance();
			if (transHdl instanceof DataBaseAware){
				((DataBaseAware) transHdl).setDBControl(this.db);
			}
			if (transHdl instanceof ITransitionHandler){
				((ITransitionHandler) transHdl).transBack(conn, document, trans ,sid, usr ,msg);
			}else{
				throw new Exception(trans.getExecute() + "不是ITransitionHandler接口的实现类！");
			}
		}
	}
}
