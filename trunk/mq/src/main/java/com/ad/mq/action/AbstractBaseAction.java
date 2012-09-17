package com.ad.mq.action;

import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ad.commons.util.Utils;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.ApplicationAware;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.RequestAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.DataObject;

public abstract class AbstractBaseAction extends ActionSupport implements DataBaseAware, RequestAware ,ApplicationAware ,SessionAware {

	protected Integer $dataid;
	protected DBControl db;
	private final Logger log = Logger.getLogger(AbstractBaseAction.class);
	protected Map<String, Object> request;
	protected Map<String, Object> application;
	protected Map<String, Object> session;
	/**
	 * 数据源编号
	 * 
	 * @param dataId
	 */
	public void set$dataid(Integer dataId) {
		this.$dataid = dataId;
	}

	@Override
	public void setDBControl(DBControl ctl) {
		this.db = ctl;
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
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	@Override
	public abstract void execute() throws Exception;

	/**
	 * 将实体更新至数据库
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected Integer update(Object obj) throws Exception {

		return this.db.update(obj);
	}

	
	/**
	 * 将实体更新至数据库
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected Integer update(Object obj ,Map<String ,Object> request) throws Exception {

		return this.db.update(obj ,request);
	}

	/**
	 * 将实体插入至数据库
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> insert(Object obj) throws Exception {
		return this.db.insert(obj);
	}

	/**
	 * 向实体中注入值
	 * 
	 * @param clazz
	 * @param req
	 * @return
	 * @throws Exception
	 */
	protected Object getObject(Class<?> clazz) throws Exception {
		return Utils.map2Object(clazz, this.request ,this.application ,this.session);
	}

	/**
	 * 根据数据ID得到数据源实体
	 * 
	 * @param dataid
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected Class<?> getEntityClass(Object dataid) throws SQLException, ClassNotFoundException {
		DataObject dataObj = (DataObject) this.db.query2Bean(this.cfg, DataObject.class, new Object[] { dataid });
		if (null == dataObj && log.isDebugEnabled()) {
			log.debug("dataId:" + this.$dataid + "不存在");
			return null;
		}
		if ((null == dataObj.getBeanname() || dataObj.getBeanname().isEmpty()) && log.isDebugEnabled()) {
			log.debug("dataId:" + this.$dataid + "没有指定实体类名！");
			return null;
		}
		return Class.forName(dataObj.getBeanname());
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

}
