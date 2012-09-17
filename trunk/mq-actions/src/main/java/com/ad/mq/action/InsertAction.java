package com.ad.mq.action;

import java.util.Map;
import com.ad.mq.model.OutData;

/**
 * 插入数据Action
 * 
 * @author YMQ
 * 
 */
public class InsertAction extends AbstractBaseAction {

	@Override
	public void execute() throws Exception {
		OutData data = new OutData();
		data.setData(this.doInsert(this.$dataid));
		this.out = data;
	}

	protected Map<String, Object> doInsert(Integer did) throws Exception {
		Class<?> clzz = this.getEntityClass(did);
		Object object = this.getObject(clzz);
		return this.insert(object);
	}
}
