package com.ad.mq.db.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.commons.util.Utils;
import com.ad.mq.db.Page;

/**
 * Oracle访问实现
 * 
 * @author YMQ
 * 
 */
public class Oracle extends AbstractDataBase {

	private final Logger log = Logger.getLogger(Oracle.class);

	public String pageTransfer(String sql, Page page, Object[] values) throws SQLException {

		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) total_ from (").append(sql).append(")");
		BigDecimal total = (BigDecimal) this.query2Scalar(sb.toString(), "total_", values);
		page.setTotal(total.longValue());
		sb.delete(0, sb.length());

		// int pageNo = (page.getPageNo() < 1) ? 1 : page.getPageNo();
		// int limit = (page.getLimit() < 0) ? 0 : page.getLimit();
		// page.setStartIndex((pageNo - 1) * limit);
		page.setEndIndex(page.getStartIndex() + page.getLimit());
		sb.append("select * from (select row_.*,rownum rownum_ from (").append(sql).append(") row_ where rownum <=").append(page.getEndIndex()).append(") where rownum_>").append(page.getStartIndex());
		if (log.isDebugEnabled()) {
			log.debug(sb.toString());
		}
		return sb.toString();
	}

	@Override
	public Map<String, Object> insert(Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		Map<String, Object> generatedValue = generatedValue(o);
		String sql = Utils.getInsertSQL(o, generatedValue, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		this.update(sql, values.toArray());
		return generatedValue;
	}

	@Override
	public Map<String, Object> insert(Connection con, Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		Map<String, Object> generatedValue = generatedValue(con, o);
		String sql = Utils.getInsertSQL(o, generatedValue, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		this.update(con, sql, values.toArray());
		return generatedValue;
	}

	/**
	 * 自生成值
	 * 
	 * @param o
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> generatedValue(Object o) throws Exception {
		Map<String, Object> values = new Hashtable<String, Object>();
		Map<String, String> seq = getGenerateFields(o);
		Object value;
		for (String name : seq.keySet()) {
			value = this.query2Array("SELECT " + seq.get(name) + ".nextval FROM DUAL", new Object[] {})[0];
			values.put(name, value);
		}
		return values;
	}

	/**
	 * 自生成值
	 * 
	 * @param conn
	 * @param o
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> generatedValue(Connection conn, Object o) throws Exception {
		Map<String, Object> values = new Hashtable<String, Object>();
		Map<String, String> seq = getGenerateFields(o);
		Object value;
		for (String name : seq.keySet()) {
			value = this.query2Array(conn, "SELECT " + seq.get(name) + ".nextval FROM DUAL", new Object[] {})[0];
			values.put(name, value);
		}
		return values;
	}

	/**
	 * 获取属性及其生产策略
	 * 
	 * @param o
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private Map<String, String> getGenerateFields(Object o) throws Exception {
		Map<String, String> seq = new Hashtable<String, String>();
		Class<?> clzz = o.getClass();
		GeneratedValue gv;
		SequenceGenerator sg;
		Object value;
		while (null != clzz) {
			for (Field field : clzz.getDeclaredFields()) {
				if (null != field.getAnnotation(GeneratedValue.class) && null != field.getAnnotation(SequenceGenerator.class)) {
					value = Utils.invokeGet(o, field.getName());
					if (value == null || (((value instanceof Integer) && (((Integer) value) == 0)) || ((value instanceof Long) && (((Long) value) == 0L)))) {

						gv = (GeneratedValue) field.getAnnotation(GeneratedValue.class);
						sg = (SequenceGenerator) field.getAnnotation(SequenceGenerator.class);
						if (gv.generator().equalsIgnoreCase(sg.name()) && !StringUtils.isEmpty(sg.sequenceName())) {
							seq.put(field.getName(), sg.sequenceName());
						}
					}
				}
			}
			clzz = clzz.getSuperclass();
		}
		return seq;
	}

	@Override
	public ResultSetMetaData query2ResultSetMetaData(String sql) throws SQLException, IOException {
		Connection conn = this.getDataSource().getConnection();
		return this.query2ResultSetMetaData(conn, sql);
	}

	@Override
	public ResultSetMetaData query2ResultSetMetaData(Connection con, String sql) throws SQLException, IOException {
		return this.query2ResultSetMetaData(con, sql, new Object[]{});
	}

	@Override
	public ResultSetMetaData query2ResultSetMetaData(Connection con, String sql, Object[] params) throws SQLException, IOException {
		PreparedStatement ps = con.prepareStatement("Select * from (" + sql + ") Where rownum <2");
		
		return AbstractDataBase.putValue2Statement(ps, params).executeQuery().getMetaData();
	}


	
	
}
