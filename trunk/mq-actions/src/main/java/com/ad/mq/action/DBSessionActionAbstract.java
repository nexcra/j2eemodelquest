package com.ad.mq.action;

import java.util.Map;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;

public abstract class DBSessionActionAbstract extends ActionSupport implements DataBaseAware,SessionAware{

	protected Map<String, Object> session;
	protected DBControl db;
	
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
		this.db = dbcontrol;
	}

}
