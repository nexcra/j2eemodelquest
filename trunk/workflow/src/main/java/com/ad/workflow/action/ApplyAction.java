package com.ad.workflow.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.workflow.IWorkFlow;
import com.ad.workflow.service.DefaultWorkFlowService;

public class ApplyAction extends ActionSupport implements DataBaseAware, SessionAware {

	private Map<String, Object> session;
	private DBControl db;
	private Integer wfid;
	private Logger log = Logger.getLogger(ApplyAction.class);

	public void setWfid(Integer wfid) {
		this.wfid = wfid;
	}

	@Override
	public void execute() throws Exception {
		OutData outdata = new OutData();
		this.out = outdata;
		Connection conn = null;
		DefaultWorkFlowService service = new DefaultWorkFlowService();
		service.setDb(this.db);
		IWorkFlow workflow = service;
		try {
			conn = this.db.getDataSource().getConnection();
			conn.setAutoCommit(false);
			IUser usr = (IUser) this.session.get("user");
			outdata.setData(workflow.Start(conn, this.wfid, usr));
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			log.error(e);
			outdata.setMessage(e.getMessage());
			throw e;
		} finally {
			DbUtils.close(conn);
		}
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

}
