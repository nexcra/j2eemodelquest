package com.ad.workflow.service;

import java.sql.Connection;
import java.util.List;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.workflow.ITransitionHandler;
import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.impl.DefaultWorkFlowContext;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultBackService implements DataBaseAware {

	private DBControl db;
	// private static String SQL =
	// "select * from WORKFLOW$TRANSITION where tonode = ?";
	private static String SQL = "select * from WORKFLOW$TRANSITION a ,WORKFLOW$DOCUMENT$STEPS b  " 
											+ " where a.tonode =? and A.FROMNODE = b.nid and b.did = ? order by B.ID desc";

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@SuppressWarnings("unchecked")
	public void back(Connection conn, Integer did, Integer nid, Integer sid, Integer usrid, String msg, Integer type) throws Exception {
		IWorkFlowContext cxt = DefaultWorkFlowContext.getInstance();
		// Connection conn = this.db.getDataSource().getConnection();
		VWorkFlowDocument document = cxt.getVWorkFlowDocument(conn, did);
		// System.out.println(SQL);

		List<WorkFlowTransition> transitions;
		transitions = (List<WorkFlowTransition>) this.db.query2BeanList(SQL, WorkFlowTransition.class, new Object[] { nid, did });
		WorkFlowTransition trans = transitions.get(0);
		Object  transHdl = Class.forName(trans.getExecute()).newInstance();
		if (transHdl instanceof DataBaseAware) {
			((DataBaseAware) transHdl).setDBControl(this.db);
		}
		if (transHdl instanceof ITransitionHandler) {
			((ITransitionHandler) transHdl).transBack(conn, document, trans, sid, trans.getUsrid(), msg);
		} else {
			throw new Exception(trans.getExecute() + "不是ITransitionHandler接口的实现类！");
		}
		/*
		Object transHdl;
		Integer currentId = 0;
		for (WorkFlowTransition trans : transitions) {
			if (currentId == trans.getId()){
				continue;
			}
			currentId = trans.getId();
			transHdl = Class.forName(trans.getExecute()).newInstance();
			if (transHdl instanceof DataBaseAware) {
				((DataBaseAware) transHdl).setDBControl(this.db);
			}
			if (transHdl instanceof ITransitionHandler) {
				((ITransitionHandler) transHdl).transBack(conn, document, trans, sid, usrid, msg);
			} else {
				throw new Exception(trans.getExecute() + "不是ITransitionHandler接口的实现类！");
			}
		}*/
	}
}
