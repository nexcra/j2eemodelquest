package com.ad.mq.interceptor;

import javax.servlet.http.Cookie;

/**
 * 设置Cookie
 * @author YMQ
 *
 */
public interface CookiesAware {
	void setCookies(Cookie[] cookies);
}
