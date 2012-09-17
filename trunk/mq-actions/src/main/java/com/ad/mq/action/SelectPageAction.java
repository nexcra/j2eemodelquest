package com.ad.mq.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.ad.mq.db.Page;
import com.ad.mq.interceptor.PagingAware;
import com.ad.mq.model.OutData;

/**
 * 根据数据源查询数据可以分页 分页需要传入: limit:每页行数 start:第几页
 * 
 * @author YMQ
 * 
 */
public class SelectPageAction extends SelectAction implements PagingAware {

	private final Logger log = Logger.getLogger(SelectPageAction.class);
	private Page page;

	@Override
	public void execute() throws Exception {
		putSQL();
		List<?> list = this.db.query2BeanList(this.page, this.sql, clzz, datas.toArray());
		OutData od = new OutData();
//		od.setCfg(this.dataCfg);
		od.setData(list);
		od.setTotal(this.page.getTotal());
		this.out = od;
		if (log.isDebugEnabled()) {
			log.debug("page.start=" + this.page.getStartIndex());
			log.debug("page.end=" + this.page.getEndIndex());
		}
	}

	@Override
	public void setPage(Page page) {
		this.page = page;
	}

}
