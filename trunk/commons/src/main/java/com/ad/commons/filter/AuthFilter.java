package com.ad.commons.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class AuthFilter implements Filter {

	private final Logger log = Logger.getLogger(AuthFilter.class);
	private String exclusive = null;
	private Map<String, String> redirect = new HashMap<String, String>();

	@Override
	public void destroy() {
		this.redirect.clear();
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpSession session = request.getSession(false);
		if (log.isDebugEnabled()) {
			log.info(request.getHeader("Referer"));
		}
		String referURI = request.getHeader("Referer").substring(request.getHeader("Referer").indexOf(request.getContextPath(), 1));
		String targetURI = request.getRequestURI().substring(request.getRequestURI().indexOf("/", 1));
		String suffix = null;

		if (log.isDebugEnabled()) {
			log.debug(request.getHeader("Referer"));
			log.debug("referURI:" + referURI);
			log.debug(request.getRequestURI());
			log.debug("targetURI:" + targetURI);
		}

		if (targetURI.indexOf('.') != -1) {
			suffix = targetURI.substring(targetURI.indexOf('.'), targetURI.length());
		} else {
			suffix = targetURI;
		}
		
		if (this.validate(referURI)) {
			if (session == null || session.getAttribute("user") == null) {
				response.sendRedirect(this.redirect.get(suffix));
				return;
			}
		}
		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		exclusive = arg0.getInitParameter("exclusive");
		exclusive = StringUtils.replace(exclusive, "{contextPath}", arg0.getServletContext().getContextPath());
		String rds = arg0.getInitParameter("redirect");
		if (!StringUtils.isEmpty(rds)) {
			String[] a1 = StringUtils.split(rds, ',');
			String key;
			String value;
			for (String string : a1) {
				key = StringUtils.split(string, '=')[0];
				value = StringUtils.split(string, '=')[1];
				value = StringUtils.replace(value, "{contextPath}", arg0.getServletContext().getContextPath());
				redirect.put(key, value);
			}
		}
	}

	/**
	 * 查找请求URI是否在不验证的范围内
	 * 
	 * @param url
	 * @return
	 */
	public Boolean validate(String uri) {
		if (StringUtils.isEmpty(this.exclusive)) {
			return true;
		}
		if (StringUtils.contains(this.exclusive, uri)) {
			return false;
		}
		return true;
	}

}
