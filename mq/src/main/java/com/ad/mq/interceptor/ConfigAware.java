package com.ad.mq.interceptor;

/**
 * 来自MQ$Action表的cfg
 * @author YMQ
 *
 */
public interface ConfigAware {
	void setConfig(String cfg);
}
