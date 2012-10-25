package com.ad.mq.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.commons.cfg.App;
import com.ad.commons.util.JSONUtil;
import com.ad.commons.util.Utils;
import com.ad.mq.db.DBControl;
import com.ad.mq.db.DataBaseFactory;
import com.ad.mq.db.Page;
import com.ad.mq.interceptor.ApplicationAware;
import com.ad.mq.interceptor.ConfigAware;
import com.ad.mq.interceptor.ContentTypeAware;
import com.ad.mq.interceptor.CookiesAware;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.HeaderAware;
import com.ad.mq.interceptor.IAction;
import com.ad.mq.interceptor.InputStream2StringAware;
import com.ad.mq.interceptor.MultipartAware;
import com.ad.mq.interceptor.PagingAware;
import com.ad.mq.interceptor.PrintWriterAware;
import com.ad.mq.interceptor.RequestAware;
import com.ad.mq.interceptor.ServletRequestAware;
import com.ad.mq.interceptor.ServletResponseAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.DataAction;

/**
 * 控制器
 * 
 * @author Administrator
 * 
 */
@MultipartConfig
public class MQControl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(MQControl.class);
	private String jndiName;
	private String dbType = "oracle";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String jndi = config.getServletContext().getInitParameter("MQ-jndiName");
		if (null == jndi || jndi.isEmpty()) {
			LOG.error("jndiName 不存在，请配置！");
			throw new ServletException("jndiName 不存在，请配置！");
		}
		jndiName = jndi;

		String dbTypeParam = config.getInitParameter("MQ-dbType");
		if (null != dbTypeParam && !dbTypeParam.isEmpty()) {
			this.dbType = dbTypeParam;
		}
		if (Logger.getRootLogger().isDebugEnabled()) {
			LOG.debug("jndiName:" + this.jndiName);
			LOG.debug("dbType:" + this.dbType);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.execute(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionid = req.getParameter("$actionid");
		String method = req.getParameter("$method");
		if (Logger.getRootLogger().isDebugEnabled()) {
			LOG.debug("$actionid:" + actionid);
			LOG.debug("$method:" + method);
		}
		if (null == actionid || actionid.isEmpty()) {
			throw new ServletException("$actionid is null");
		}
		DBControl db = null;
		try {

			db = DataBaseFactory.getDBControl(this.dbType, this.jndiName);
			DataAction dataAction = (DataAction) db.query2Bean("select * from MQ$ACTION where id=? and status=1", DataAction.class, new Object[] { actionid });
			if (null == dataAction) {
				throw new ServletException("$actionid:" + actionid + " 不存在或不可用！");
			}
			String serviceClassName = dataAction.getService();
			Object action = Class.forName(serviceClassName).newInstance();

			putFieldValue(action, req);
			
			if (action instanceof SessionAware) {
				((SessionAware) action).setSession(this.getSessionMap(req));
			}

			if (action instanceof ConfigAware) {
				((ConfigAware) action).setConfig(dataAction.getCfg());
			}
			if (action instanceof CookiesAware) {
				((CookiesAware) action).setCookies(req.getCookies());
			}

			if (action instanceof HeaderAware) {
				((HeaderAware) action).setHeader(this.getHeader(req));
			}

			if (action instanceof ApplicationAware) {
				((ApplicationAware) action).setApplication(this.getApplicationMap());
			}
			if (action instanceof DataBaseAware) {
				((DataBaseAware) action).setDBControl(db);
			}
			if (action instanceof RequestAware) {
				((RequestAware) action).setRequest(this.getRequestMap(req));
			}
			if (action instanceof ServletRequestAware) {
				((ServletRequestAware) action).setServletRequest(req);
			}

			if (action instanceof ServletResponseAware) {
				((ServletResponseAware) action).setServletResponse(resp);
			}



			if (action instanceof MultipartAware) {
				((MultipartAware) action).setParts(req.getParts());
			}

			if (action instanceof InputStream2StringAware) {
				((InputStream2StringAware) action).setInputString(Utils.inputStream2String(req.getInputStream()));
			}

			if (action instanceof PagingAware) {
				Page p = new Page();
				String limit = req.getParameter("$limit");
				String start = req.getParameter("$start");
				if (null != limit && StringUtils.isNumeric(limit)) {
					p.setLimit(Integer.parseInt(limit));
				}
				if (null != start && StringUtils.isNumeric(start)) {
					p.setStartIndex(Integer.parseInt(start));
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("page.limit:" + limit + ",page.start:" + start);
				}
				((PagingAware) action).setPage(p);
			}

			if ((null == method || method.isEmpty()) && (action instanceof IAction)) {
				((IAction) action).execute();
			} else {
				Method md = action.getClass().getMethod(method, new Class[] {});
				md.invoke(action, new Object[] {});
				if (LOG.isDebugEnabled()) {
					LOG.debug(method + "() is invoked!");
				}
			}

			if (action instanceof SessionAware) {
				map2Session(((SessionAware) action).getSession(), req);
			}

			if (action instanceof PrintWriterAware) {
				Object data = ((PrintWriterAware) action).getOut();
				if (null != data) {
					String contentType = null;
					if (action instanceof ContentTypeAware) {
						contentType = ((ContentTypeAware) action).getContentType();
						resp.setContentType(contentType);
					}
					if (data instanceof String) {

					} else {
						if (contentType == App.CONTENTTYPE_JSON || contentType == App.CONTENTTYPE_TEXT_XML) {
							JsonConfig jc = this.registerJsonValueProcessor();
							if (data instanceof Collection || data instanceof Object[]) {
								data = JSONArray.fromObject(data, jc);
							} else {
								try {
									data = JSONObject.fromObject(data, jc);
								} catch (Exception e) {
//									LOG.error(data);
									LOG.error(e);
								}
							}
							if (contentType == App.CONTENTTYPE_TEXT_XML) {
								data = new XMLSerializer().write((JSON) data);
							}
						}
					}
					PrintWriter out = resp.getWriter();
					out.print(data);
				}
			}

		} catch (Exception e) {
			LOG.error(e);
			throw new ServletException(e);
		} finally {
		}

	}

	/**
	 * Map 转成session
	 * 
	 * @param map
	 * @param req
	 */
	private void map2Session(Map<String, Object> map, HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (map == null || map.isEmpty()) {
			session.invalidate();
		}
		Set<String> keys = map.keySet();
		for (String key : keys) {
			session.setAttribute(key, map.get(key));
		}
	}

	/**
	 * 获取所有Session参数
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, Object> getSessionMap(HttpServletRequest req) {
		Map<String, Object> maps = new Hashtable<String, Object>();
		HttpSession session = req.getSession();
		Enumeration<String> enums = session.getAttributeNames();
		String name;
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			maps.put(name, session.getAttribute(name));
		}
		return maps;
	}

	/**
	 * 获取所有Request参数
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, Object> getRequestMap(HttpServletRequest req) {
		Map<String, Object> maps = new Hashtable<String, Object>();
		Enumeration<String> enums = req.getAttributeNames();
		String name;
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			maps.put(name, req.getAttribute(name));
		}
		enums = req.getParameterNames();
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			maps.put(name, req.getParameter(name));
		}
		return maps;
	}

	/**
	 * 获取Application参数
	 * 
	 * @return
	 */
	private Map<String, Object> getApplicationMap() {
		Map<String, Object> maps = new Hashtable<String, Object>();
		ServletContext context = this.getServletConfig().getServletContext();
		Enumeration<String> enums = context.getAttributeNames();
		String name;
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			maps.put(name, context.getAttribute(name));
		}
		enums = context.getInitParameterNames();
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			maps.put(name, context.getInitParameter(name));
		}
		return maps;
	}

	/**
	 * 向action里加载数据
	 * 
	 * @param obj
	 * @param req
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void putFieldValue(Object obj, HttpServletRequest req) throws IOException, ServletException, ParseException {
		Class<?>[] parameterTypes = new Class[1];
		Class<?> clazz = obj.getClass();
		String fieldName = null;
		Object value = null;
		while (null != clazz) {
			for (Field field : clazz.getDeclaredFields()) {
				parameterTypes[0] = field.getType();
				fieldName = field.getName();
				if (parameterTypes[0].isArray()) {
					value = Utils.transferTypes(req, fieldName, parameterTypes[0].getComponentType());
				} else {
					if (parameterTypes[0] == Part.class) {
						value = req.getPart("fieldName");
					} else {
						value = (null == req.getParameter(fieldName) ? req.getAttribute(fieldName) : req.getParameter(fieldName));
						try {
							value = Utils.transferType(value, parameterTypes[0]);
						} catch (ParseException e1) {
							LOG.error(e1);
							continue;
						}
					}

				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("fieldName:" + fieldName + ",value=" + value);
				}
				if (null == value) {
					continue;
				}

				StringBuffer sb = new StringBuffer();
				sb.append("set");
				sb.append(fieldName.substring(0, 1).toUpperCase());
				sb.append(fieldName.substring(1));
				try {
					Method method = clazz.getMethod(sb.toString(), parameterTypes);
					method.invoke(obj, new Object[] { value });
					if (LOG.isDebugEnabled()) {
						LOG.debug(sb.toString() + "(" + value + ")");
					}
				} catch (Exception e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug(e);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * 向action里加载头信息
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, String> getHeader(HttpServletRequest req) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("$AuthType", req.getAuthType());
		header.put("$ContentLength", Integer.toString(req.getContentLength()));
		header.put("$ContentType", req.getContentType());
		header.put("$ContextPath", req.getContextPath());
		header.put("$LocalAddr", req.getLocalAddr());
		header.put("$LocalName", req.getLocalName());
		// req.getCookies()
		header.put("$LocalPort", Integer.toString(req.getLocalPort()));
		header.put("$PathInfo", req.getPathInfo());
		header.put("$PathTranslated", req.getPathTranslated());
		header.put("$Protocol", req.getProtocol());
		header.put("$QueryString", req.getQueryString());
		header.put("$RemoteAddr", req.getRemoteAddr());
		header.put("$RemoteHost", req.getRemoteHost());
		header.put("$RemotePort", Integer.toString(req.getRemotePort()));
		header.put("$RemoteUser", req.getRemoteUser());
		header.put("$RequestedSessionId", req.getRequestedSessionId());
		header.put("$RequestURL", req.getRequestURL().toString());
		header.put("$RequestURI", req.getRequestURI());
		header.put("$Scheme", req.getScheme());
		header.put("$ServerName", req.getServerName());
		header.put("$ServerPort", Integer.toString(req.getServerPort()));
		header.put("$ServletPath", req.getServletPath());
		Enumeration<String> enums = req.getHeaderNames();
		String name;
		while (enums.hasMoreElements()) {
			name = enums.nextElement();
			header.put(name, req.getHeader(name));
		}
		return header;
	}

	public JsonConfig registerJsonValueProcessor() {
		JsonConfig jsonConfig = new JsonConfig();
		JSONUtil beanProcessor = new JSONUtil();
		jsonConfig.registerJsonValueProcessor(Date.class, beanProcessor);
		jsonConfig.registerJsonValueProcessor(Time.class, beanProcessor);
		jsonConfig.registerJsonValueProcessor(Timestamp.class, beanProcessor);
		return jsonConfig;
	}
}
