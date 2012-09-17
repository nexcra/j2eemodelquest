package com.ad.mq.interceptor;

import java.util.Map;
/**
 * 请求头信息
 * @author YMQ
 *
 */
public interface HeaderAware {
	void setHeader(Map<String, String> heads);
}
