package com.ad.commons.filter;

import java.io.IOException;

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

public class MQFilter implements Filter {

	private String exclusiveActionids;
	private String redirectUrl;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String actionid = request.getParameter("$actionid");
		if (!StringUtils.isEmpty(exclusiveActionids) && !StringUtils.isEmpty(actionid) && !StringUtils.contains(exclusiveActionids, "|" + actionid + "|")) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("user") == null) {
				if (!StringUtils.isEmpty(this.redirectUrl)) {
					this.redirectUrl = StringUtils.replace(this.redirectUrl, "{contextPath}", request.getServletContext().getContextPath());
					res.sendRedirect(this.redirectUrl);
				}
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		exclusiveActionids = arg0.getInitParameter("exclusive_actionids");
		redirectUrl = arg0.getInitParameter("redirect_url");
	}

}
