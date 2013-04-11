package com.ad.workflow.action;

import java.util.Map;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.OutData;

/**
 * 删除案例，将案例设成无效
 * 
 * @author YMQ
 * 
 */
public class DeleteDocumentAction extends ActionSupport implements DataBaseAware, SessionAware {

	private Map<String, Object> session;
	private DBControl dbctl;
	private Integer did;
	private static String SQL = " update WORKFLOW$DOCUMENT set status = 0 where id =?";

	public void setDid(Integer did) {
		this.did = did;
	}

	@Override
	public Map<String, Object> getSession() {
		return this.session;
	}

	@Override
	public void setSession(Map<String, Object> map) {
		this.session = map;
	}

	@Override
	public void setDBControl(DBControl dbcontrol) {
		this.dbctl = dbcontrol;
	}

	@Override
	public void execute() throws Exception {
		OutData data = new OutData();
		this.out = data;
		this.dbctl.update(SQL, new Object[] { did });

	}

}
