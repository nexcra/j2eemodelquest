package com.ad.mq.interceptor;

import com.ad.mq.db.Page;
/**
 * 分页
 * @author YMQ
 *
 */
public interface PagingAware {
	void setPage(Page page);
}
