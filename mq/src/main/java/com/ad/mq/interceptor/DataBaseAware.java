package com.ad.mq.interceptor;

import com.ad.mq.db.DBControl;

/**
 * 数据库控制
 * @author YMQ
 *
 */
public interface DataBaseAware {
	void setDBControl(DBControl ctl);
}
