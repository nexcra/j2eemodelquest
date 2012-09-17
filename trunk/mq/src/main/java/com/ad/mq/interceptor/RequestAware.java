package com.ad.mq.interceptor;

import java.util.Map;
/**
 * Request参数
 * @author YMQ
 *
 */
public interface RequestAware {
	void setRequest(Map<String ,Object> request);
}
