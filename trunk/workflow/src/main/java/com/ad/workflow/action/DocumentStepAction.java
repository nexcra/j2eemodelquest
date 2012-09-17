package com.ad.workflow.action;

import java.util.Map;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.OutData;
import com.ad.workflow.IWorkFlowContext;
import com.ad.workflow.impl.DefaultWorkFlowContext;
import com.ad.workflow.service.DefaultWorkFlowStepService;

public class DocumentStepAction extends ActionSupport implements DataBaseAware, SessionAware {
	private Map<String, Object> session;
	private DBControl db;
	private Integer sid;
	private String msg;

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public Map<String, Object> getSession() {
		return this.session;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@Override
	public void execute() throws Exception {
		DefaultWorkFlowStepService service = new DefaultWorkFlowStepService();
		service.setDBControl(this.db);
		service.saveMsg(this.db.getDataSource().getConnection(), sid, msg);

	}

	public void getStep() throws Exception {
		IWorkFlowContext cxt = DefaultWorkFlowContext.getInstance();
		OutData outdata = new OutData();
		this.out = outdata;

		outdata.setData(cxt.getWorkFlowDocumentStep(this.db.getDataSource().getConnection(), this.sid));

	}
}
