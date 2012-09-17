package com.ad.mq.interceptor;

import java.util.Map;

/**
 * Session参数
 * @author YMQ
 *
 */
public interface SessionAware {
	void setSession(Map<String,Object> session);
	Map<String ,Object> getSession();
}
