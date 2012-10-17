package com.ad.mq.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.commons.cfg.App;
import com.ad.commons.util.Utils;
import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.ApplicationAware;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.RequestAware;
import com.ad.mq.interceptor.SessionAware;
import com.ad.mq.model.DataObject;
import com.ad.mq.model.Filter;
import com.ad.mq.model.OutData;
import com.ad.mq.model.Sorter;

/**
 * 根据数据源查询数据
 * 
 * @author YMQ
 * 
 */
public class SelectAction extends ActionSupport implements DataBaseAware, ApplicationAware, RequestAware, SessionAware {

	protected Integer $dataid;// 数据源编号
//	protected Integer did;// 数据源结构dataid
	protected DBControl db;
	protected Map<String, Object> session;
	protected Map<String, Object> request;
	protected Map<String, Object> application;
	protected static final String REGX_SQL = "\\{\\s*[application|session|request]\\s*.\\s*([^}]*)\\s*\\}";
	protected String sql;
	protected List<Object> datas;
	protected Class<?> clzz;
	protected String $filter;
	protected String $sort;
	protected String $queryvalue;
	protected String $querynames;
//	protected String dataCfg;

	private final Logger log = Logger.getLogger(SelectAction.class);

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	/**
	 * 数据源编号
	 * 
	 * @param dataId
	 */
	public void set$dataid(Integer $dataid) {
		this.$dataid = $dataid;
	}

//	public void setDid(Integer did) {
//		this.did = did;
//	}

	/**
	 * 模糊查询值
	 * 
	 * @param $queryvalue
	 */
	public void set$queryvalue(String $queryvalue) {
		this.$queryvalue = $queryvalue;
	}

	/**
	 * 模糊查询字段名s
	 * 
	 * @param $querynames
	 */
	public void set$querynames(String $querynames) {
		this.$querynames = $querynames;
	}

	/**
	 * 过滤条件
	 * 
	 * @param filter
	 */
	public void set$filter(String filter) {
		this.$filter = filter;
	}

	/**
	 * 排序条件
	 * 
	 * @param sort
	 */
	public void set$sort(String sort) {
		this.$sort = sort;
	}

	@Override
	public void execute() throws Exception {
		putSQL();
		List<?> list = this.db.query2BeanList(this.sql, clzz, datas.toArray());
		OutData od = new OutData();
//		od.setCfg(this.dataCfg);
		od.setData(list);
		this.out = od;

	}

	/**
	 * 组织数据
	 * 
	 * @throws Exception
	 */
	protected void putSQL() throws Exception {

		DataObject dataObj = (DataObject) this.db.query2Bean(this.cfg, DataObject.class, new Object[] { this.$dataid });
		if (null == dataObj) {
			log.error("dataId:" + this.$dataid + "不存在");
			throw new Exception("dataId:" + this.$dataid + "不存在");
		}

		this.sql = dataObj.getSql();
		datas = putData4SQL();
		String beanName = dataObj.getBeanname();
		clzz = Class.forName(beanName);
		putFilterAndSorter();

//		if (null != this.did) {// 查询配制信息时
//			DataObject dObj = (DataObject) this.db.query2Bean(this.cfg, DataObject.class, new Object[] { this.did });
//			this.dataCfg = dObj.getCfg();
//		}
	}

	/**
	 * 设置WHERE和ORDER BY
	 */
	protected void putFilterAndSorter() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (").append(this.sql).append(") MQ_ ");
		if (!StringUtils.isEmpty(this.$filter)) {
			sb.append("WHERE ");
			JSONArray array = JSONArray.fromObject(this.$filter);
			Filter filter;
			for (Object object : array) {
				filter = (Filter) JSONObject.toBean(JSONObject.fromObject(object), Filter.class);
				if (App.EXTJS_DATATYPE_INT.equalsIgnoreCase(filter.getType()) || App.EXTJS_DATATYPE_FLOAT.equalsIgnoreCase(filter.getType())
						|| App.EXTJS_DATATYPE_NUMERIC.equalsIgnoreCase(filter.getType())) {
					sb.append("MQ_.").append(filter.getField());
					if ("lt".equalsIgnoreCase(filter.getComparison())) {
						sb.append("<");
					} else if ("gt".equalsIgnoreCase(filter.getComparison())) {
						sb.append(">");
					} else {
						sb.append("=");
					}
					sb.append(filter.getValue()).append(" AND ");
				} else if (App.EXTJS_DATATYPE_STRING.equalsIgnoreCase(filter.getType())) {
					sb.append("MQ_.").append(filter.getField()).append(" LIKE '%").append(filter.getValue()).append("%'").append(" AND ");
				} else if (App.EXTJS_DATATYPE_DATE.equalsIgnoreCase(filter.getType())) {
					sb.append("TO_CHAR(MQ_.").append(filter.getField()).append(",'yyyy-MM-dd')");
					if ("lt".equalsIgnoreCase(filter.getComparison())) {
						sb.append("<");
					} else if ("gt".equalsIgnoreCase(filter.getComparison())) {
						sb.append(">");
					} else {
						sb.append("=");
					}
					sb.append("'").append(filter.getValue()).append("' AND ");
				}else if (App.EXTJS_DATATYPE_LIST.equalsIgnoreCase(filter.getType())) {
					sb.append("MQ_.").append(filter.getField()).append(" IN ('");
					String value =filter.getValue();
					value =StringUtils.replace(value, ",", "','");
					sb.append(value).append("') AND ");
				}
			}
			sb.append(" 1=1 ");
		}
		if (!StringUtils.isEmpty(this.$sort)) {
			sb.append("ORDER BY ");
			JSONArray array = JSONArray.fromObject(this.$sort);
			Sorter sorter;
			for (Object object : array) {
				sorter = (Sorter) JSONObject.toBean(JSONObject.fromObject(object), Sorter.class);
				if ("desc".equalsIgnoreCase(sorter.getDirection())) {
					sb.append(" MQ_.").append(sorter.getProperty()).append(" DESC,");
				} else {
					sb.append(" MQ_.").append(sorter.getProperty()).append(" ASC,");
				}
			}
			if (",".equalsIgnoreCase(sb.substring(sb.length() - 1, sb.length()))) {
				sb.delete(sb.length() - 1, sb.length());
			}
		}

		this.sql = sb.toString();
		if (!StringUtils.isEmpty(this.$queryvalue) && !StringUtils.isEmpty(this.$querynames)) {
			this.sql = "SELECT * FROM (" + this.sql + ") WHERE " + this.$querynames + "LIKE '%" + this.$queryvalue + "%'";
		}
		if (log.isDebugEnabled()) {
			log.debug(this.sql);
		}

	}

	/**
	 * 从Application ,Session ,Request 中提取参数替代sql
	 * 
	 * @return
	 * @throws Exception
	 */
	protected List<Object> putData4SQL() throws Exception {
		Pattern pattern = Pattern.compile(REGX_SQL);
		List<Object> datas = new ArrayList<Object>();
		Matcher m = pattern.matcher(sql);
		String tmp;
		String scope;
		String key;
		String method;
		Object value = null;
		while (m.find()) {
			this.sql = StringUtils.replace(this.sql, m.group(), "?");
			tmp = m.group().replaceAll("\\s*", "").replaceAll("\\{", "").replaceAll("\\}", "");
			scope = StringUtils.split(tmp, "[.]")[0];
			key = StringUtils.split(tmp, "[.]")[1];

			if (scope.equalsIgnoreCase("application")) {
				value = this.application.get(key);
				datas.add(value);
			} else if (scope.equalsIgnoreCase("session")) {
				method = StringUtils.split(tmp, "[.]")[2];
				value = Utils.invokeGet(this.session.get(key), method);
				datas.add(value);
			} else if (scope.equalsIgnoreCase("request")) {
				value = this.request.get(key);
				datas.add(value);
			}
			if (log.isDebugEnabled()) {
				log.debug("scope:" + scope + ",key:" + key + ",value:" + ((null == value) ? "null" : value.toString()));
			}
		}
		return datas;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public Map<String, Object> getSession() {
		return this.session;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

}
