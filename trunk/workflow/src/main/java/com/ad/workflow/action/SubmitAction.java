package com.ad.workflow.action;

import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.workflow.service.DefaultSubmitService;

public class SubmitAction extends ActionSupport implements DataBaseAware, SessionAware {

	private Logger log = Logger.getLogger(SubmitAction.class);
	private Map<String, Object> session;
	private DBControl db;
	private Integer did;
	private Integer tid;
	private Integer sid;

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
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
		OutData outdata = new OutData();
		this.out = outdata;
		Connection conn= null;
		DefaultSubmitService service = new DefaultSubmitService();
		
		service.setDBControl(this.db);
		IUser usr = (IUser) this.session.get("user");
		try {
			conn =this.db.getDataSource().getConnection();
			conn.setAutoCommit(false);
			service.submit(conn ,did, tid, sid ,usr);
			conn.commit();
		} catch (Exception ex) {
			outdata.setMessage(ex.getLocalizedMessage());
			conn.rollback();
			log.error(ex);
			ex.printStackTrace();
		}

	}

}
