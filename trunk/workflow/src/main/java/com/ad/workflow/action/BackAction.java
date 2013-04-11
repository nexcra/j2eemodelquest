package com.ad.workflow.action;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.workflow.service.DefaultBackService;

public class BackAction extends ActionSupport implements DataBaseAware, SessionAware {

	private Logger log = Logger.getLogger(BackAction.class);
	private Map<String, Object> session;
	private DBControl db;
	private Integer did;
	private Integer sid;
	private Integer nid;
	private String msg;

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public void setDid(Integer did) {
		this.did = did;
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
		DefaultBackService service = new DefaultBackService();
		Connection conn = null;
		service.setDBControl(this.db);
		IUser usr = (IUser) this.session.get("user");
		try {
			conn = this.db.getDataSource().getConnection();
			conn.setAutoCommit(false);
			service.back(conn ,did, nid, sid, usr, msg);
			conn.commit();
		} catch (Exception ex) {
			conn.rollback();
			outdata.setMessage(ex.getLocalizedMessage());
			this.db.getDataSource().getConnection().rollback();
			log.error(ex);
			ex.printStackTrace();
			throw ex;
		}finally{
			DbUtils.close(conn);
		}

	}

}
