package com.ad.mq.action;

import com.ad.commons.cfg.App;
import com.ad.mq.interceptor.ConfigAware;
import com.ad.mq.interceptor.ContentTypeAware;
import com.ad.mq.interceptor.IAction;
import com.ad.mq.interceptor.PrintWriterAware;

public abstract class ActionSupport implements IAction, PrintWriterAware, ContentTypeAware, ConfigAware {

	protected Object out;

	protected Integer $actionid;
	protected String contentType = App.CONTENTTYPE_JSON;
	protected String cfg;

	public Object getOut() {
		return out;
	}

	public Integer get$actionid() {
		return $actionid;
	}

	public void set$actionid(Integer $actionid) {
		this.$actionid = $actionid;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setConfig(String cfg) {
		this.cfg = cfg;
	}

	public abstract void execute() throws Exception;

}
