package com.ad.workflow.impl;

import java.sql.Connection;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.ad.mq.model.IUser;
import com.ad.workflow.IValueHandler;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;
import com.ad.workflow.service.DefaultBackService;
import com.ad.workflow.service.DefaultSubmitService;

public class DefaultBranchNodeHandler extends NodeHandlerAdapter {

	@Override
	public WorkFlowDocumentStep enter(Integer fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, IUser usr) throws Exception {
		WorkFlowDocumentStep curtStep = super.enter(fromnode, node, document, conn, sid, usr);//当前步骤编号
		
		if (curtStep.getNid() <curtStep.getFromnid()){ //回退
			DefaultBackService service = new DefaultBackService();
			service.setDBControl(this.db);
			service.back(document.getId(), curtStep.getNid(), curtStep.getId(), usr, null);
		}else{ //提交
			Integer transitionId =getTransitionId(document ,curtStep ,node.getCfg());
			DefaultSubmitService service = new DefaultSubmitService();
			service.setDBControl(this.db);
			service.submit(document.getId(), transitionId, curtStep.getId(), usr);
		}
		

		return curtStep;
	}

	
	public Integer getTransitionId(VWorkFlowDocument document,WorkFlowDocumentStep step ,String cfg) throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String handlerName = null;
		String outCfg = null;
		Integer transitionId = null;
		// function getValueHandler() {return 'com.ad.mq...';} function getBranchId(value) {return c;}
		engine.eval(cfg);
		Invocable invoke = (Invocable)engine;
		handlerName = (String) invoke.invokeFunction("getValueHandler");
		outCfg = (String) invoke.invokeFunction("getValueHandlerCFG");
		IValueHandler hanlder = (IValueHandler) Class.forName(handlerName).newInstance();
		hanlder.setDBCtl(this.db);
		transitionId = (Integer) invoke.invokeFunction("getBranchId", hanlder.getValue(document, step,outCfg));
		return transitionId;
	}
}
