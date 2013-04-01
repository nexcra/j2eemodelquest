package com.ad.mq.action;
import com.ad.mq.model.OutData;

/**
 * 更新一条来自客户端提交的数据
 * 
 * @author YMQ
 * 
 */
public class UpdateAction extends AbstractBaseAction {

	@Override
	public void execute() throws Exception {

		OutData data = new OutData();
		data.setData(this.doUpdate(this.$dataid));
		this.out = data;
	}

	/**
	 * 执行数据实体更新
	 * 
	 * @param did
	 * @return
	 * @throws Exception
	 */
	protected Integer doUpdate(Integer did) throws Exception {
		Class<?> clzz = this.getEntityClass(did);
		Object object = this.getObject(clzz);
		return this.update(object,this.request);
	}

}