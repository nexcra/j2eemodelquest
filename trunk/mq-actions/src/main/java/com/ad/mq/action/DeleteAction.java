package com.ad.mq.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ad.commons.util.Utils;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.RequestAware;
import com.ad.mq.model.DataObject;
import com.ad.mq.model.OutData;

/**
 * 删除指定DataId的数据
 * 
 * @author YMQ
 * 
 */
public class DeleteAction extends ActionSupport implements DataBaseAware, RequestAware {

	protected Integer $dataid;
	protected DBControl db;
	private final Logger log = Logger.getLogger(DeleteAction.class);
	protected Map<String, Object> request;

	/**
	 * 数据源编号
	 * 
	 * @param dataId
	 */
	public void set$dataid(Integer $dataid) {
		this.$dataid = $dataid;
	}

	@Override
	public void setDBControl(DBControl ctl) {
		this.db = ctl;
	}

	@Override
	public void execute() throws Exception {
		if (null == this.$dataid && log.isDebugEnabled()) {
			log.debug("dataId:" + "没有值！");
			return;
		}
		DataObject dataObj = (DataObject) this.db.query2Bean(this.cfg, DataObject.class, new Object[] { this.$dataid });
		if (null == dataObj && log.isDebugEnabled()) {
			log.debug("dataId:" + this.$dataid + "不存在");
			return;
		}
		if ((null == dataObj.getBeanname() || dataObj.getBeanname().isEmpty()) && log.isDebugEnabled()) {
			log.debug("dataId:" + this.$dataid + "没有指定实体类名！");
			return;
		}
		Class<?> clazz = Class.forName(dataObj.getBeanname());
		Object object = Utils.map2ObjectByPK(clazz, this.request);

		Integer cunt = this.db.delete(object);
		OutData outData = new OutData();
		outData.setData(cunt);
		this.out = outData;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

}
