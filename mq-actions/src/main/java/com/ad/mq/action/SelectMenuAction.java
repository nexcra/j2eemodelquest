package com.ad.mq.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.DataObject;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.mq.model.view.VDataElement;

/**
 * 用于查的菜单对应数据源配制信息
 * 
 * @author YMQ
 * 
 */
public class SelectMenuAction extends ActionSupport implements DataBaseAware, SessionAware {
	private final Logger log = Logger.getLogger(SelectMenuAction.class);
	private Map<String, Object> session;
	private DBControl db;
	private Integer $dataid;
	private Integer token;

	public void setToken(Integer token) {
		this.token = token;
	}

	public void set$dataid(Integer $dataid) {
		this.$dataid = $dataid;
	}

	@Override
	public void execute() throws Exception {
		String[] sql = StringUtils.split(this.cfg, '\n');
		IUser user = (IUser) this.session.get("user");
		OutData data = new OutData();
		if (log.isDebugEnabled()) {
			log.debug(sql[0]);
		}
		DataObject dataObject = (DataObject) this.db.query2Bean(sql[0], DataObject.class, new Object[] { user.getUserId(), this.$dataid ,this.token });
		if (null != dataObject) {
			if (log.isDebugEnabled()) {
				log.debug(sql[1]);
			}
			@SuppressWarnings("unchecked")
			List<VDataElement> dataElement = (List<VDataElement>) this.db.query2BeanList(sql[1], VDataElement.class, new Object[] { user.getUserId(), this.$dataid });

			data.setCfg(dataObject.getCfg());
			data.setAuth(dataObject.getAuth());
			data.setToken(dataObject.getToken());
			data.setData(dataElement);
		}
		this.out = data;
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

}
