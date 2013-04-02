package com.ad.workflow.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;

public class DefaultWorkFlowStepService implements DataBaseAware {

	private DBControl db;

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	public void saveMsg(Connection conn, Integer sid, String msg) throws SQLException {
		this.db.update(conn, "update WORKFLOW$DOCUMENT$STEPS set msg  =? where id = ?", new Object[] { msg, sid });
	}

}
