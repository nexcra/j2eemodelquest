package com.ad.mq.action;

import java.util.List;
import java.util.Map;


import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.DataObject;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.mq.model.view.VDataElement;

/**
 * 查询数据源及元素
 * 
 * @author YMQ
 * 
 */
public class ShowElementsAction extends ActionSupport implements DataBaseAware, SessionAware {
	private Map<String, Object> session;
	private DBControl db;
	private Integer $dataid;
	private Integer token;
	private static final String SQL_OBJECT = "select a.* ,nvl(b.auth,0) auth from MQ$Object a ,(select * from MQ$OBJECT_AUTH where userid=?) b where a.id= b.objid(+)  and a.id=?";
	private static final String SQL_ELEMENT = "select a.* ,b.auth auth from MQ$ELEMENT a,(select * from MQ$ELEMENT_AUTH where userid=?)  b where a.id = b.elementid(+) and a.parentid=?";

	public void setToken(Integer token) {
		this.token = token;
	}

	public void set$dataid(Integer $dataid) {
		this.$dataid = $dataid;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public Map<String, Object> getSession() {
		return this.session;
	}

	@Override
	public void setDBControl(DBControl ctl) {
		this.db = ctl;
	}

	@Override
	public void execute() throws Exception {
		IUser user = (IUser) this.session.get("user");
		OutData data = new OutData();
		DataObject dataObject = (DataObject) this.db.query2Bean(SQL_OBJECT, DataObject.class, new Object[] { user.getUserId(), this.$dataid });
		if (null != dataObject) {

			data.setAuth(dataObject.getAuth());
			data.setToken(dataObject.getToken());

			if (this.token < dataObject.getToken()) {
				@SuppressWarnings("unchecked")
				List<VDataElement> dataElement = (List<VDataElement>) this.db.query2BeanList(SQL_ELEMENT, VDataElement.class, new Object[] { user.getUserId(), this.$dataid });
				data.setCfg(dataObject.getCfg());
				data.setData(dataElement);
			}
		}
		this.out = data;
	}

}
