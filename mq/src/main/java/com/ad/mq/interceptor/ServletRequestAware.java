package com.ad.mq.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest
 * @author YMQ
 *
 */
public interface ServletRequestAware {
	void setServletRequest(HttpServletRequest request);
}
