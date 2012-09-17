package com.ad.mq.interceptor;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse
 * @author YMQ
 *
 */
public interface ServletResponseAware {
	void setServletResponse(HttpServletResponse response);
}
