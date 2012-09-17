package com.ad.mq.interceptor;

import java.util.Map;
/**
 * Application参数
 * @author YMQ
 *
 */
public interface ApplicationAware {
	void setApplication(Map<String,Object> application);
}
