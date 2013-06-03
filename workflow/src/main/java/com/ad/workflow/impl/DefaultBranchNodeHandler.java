package com.ad.workflow.impl;

import java.sql.Connection;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.ad.workflow.IValueHandler;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;
import com.ad.workflow.service.DefaultBackService;
import com.ad.workflow.service.DefaultSubmitService;

public class DefaultBranchNodeHandler extends NodeHandlerAdapter {

	@Override
	public WorkFlowDocumentStep enter(Integer fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid) throws Exception {
		WorkFlowDocumentStep curtStep = super.enter(fromnode, node, document, conn, sid, usrid);// 当前步骤编号

		//this.db.update(conn, "update WORKFLOW$DOCUMENT set usrid = ? ,nid = ? where id =?", new Object[] { node.getUsrid(), node.getId(), document.getId() });
		
		if (curtStep.getNid() < curtStep.getFromnid()) { // 回退
			DefaultBackService service = new DefaultBackService();
			service.setDBControl(this.db);
			service.back(conn ,document.getId(), curtStep.getNid(), curtStep.getId(), usrid, null ,2);
			super.back(node, null, document, conn, curtStep.getId(), usrid, null);
		} else { // 提交
			Integer transitionId = 0;

			transitionId = getTransitionId(conn ,document, curtStep, node.getCfg());
			DefaultSubmitService service = new DefaultSubmitService();
			service.setDBControl(this.db);
			service.submit(conn ,document.getId(), transitionId, curtStep.getId(), usrid);

		}

		return curtStep;
	}

	@Override
	public void back(WorkFlowNode node, Integer tonid, VWorkFlowDocument document, Connection conn, Integer sid, Integer usrid, String msg) throws Exception {
		return ;
	}

	public Integer getTransitionId(Connection conn ,VWorkFlowDocument document, WorkFlowDocumentStep step, String cfg) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String handlerName = null;
		String outCfg = null;
		Integer transitionId = null;
		// function getValueHandler() {return 'com.ad.mq...';} function
		// getBranchId(value) {return c;}

		engine.eval(cfg);
		Invocable invoke = (Invocable) engine;
		handlerName = (String) invoke.invokeFunction("getValueHandler");
		outCfg = (String) invoke.invokeFunction("getValueHandlerCFG");
		IValueHandler hanlder = (IValueHandler) Class.forName(handlerName).newInstance();
		hanlder.setDBCtl(this.db);
		String returnVal = invoke.invokeFunction("getBranchId", hanlder.getValue(conn ,document, step, outCfg)).toString();
		transitionId = Integer.parseInt(returnVal);

		return transitionId;
	}
}
