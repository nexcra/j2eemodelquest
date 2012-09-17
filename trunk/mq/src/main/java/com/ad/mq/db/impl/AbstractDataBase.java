package com.ad.mq.db.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.ad.commons.util.Utils;
import com.ad.mq.db.DBControl;
import com.ad.mq.db.Page;

public abstract class AbstractDataBase implements DBControl {

	protected Logger log = Logger.getLogger(AbstractDataBase.class);
	protected DataSource ds;

	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}

	public DataSource getDataSource() {
		return this.ds;
	}

	public Object[] query2Array(String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return (Object[]) qr.query(sql, new ArrayHandler(), params);
	}

	public Object[] query2Array(Connection con, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return (Object[]) qr.query(con, sql, new ArrayHandler(), params);
	}

	public List<Object[]> query2ArrayList(String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new ArrayListHandler(), params);
	}

	public List<Object[]> query2ArrayList(Page page, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(this.pageTransfer(sql, page, params), new ArrayListHandler(), params);
	}

	public List<Object[]> query2ArrayList(Connection con, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new ArrayListHandler(), params);
	}

	public List<Object[]> query2ArrayList(Connection con, Page page, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, this.pageTransfer(sql, page, params), new ArrayListHandler(), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object query2Bean(String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new BeanHandler(clazz), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object query2Bean(Connection con, String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new BeanHandler(clazz), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> query2BeanList(String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new BeanListHandler(clazz), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> query2BeanList(Page page, String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(this.pageTransfer(sql, page, params), new BeanListHandler(clazz), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> query2BeanList(Connection con, String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new BeanListHandler(clazz), params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> query2BeanList(Connection con, Page page, String sql, Class<?> clazz, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, this.pageTransfer(sql, page, params), new BeanListHandler(clazz), params);
	}

	public List<Object> query2ColumnList(String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new ColumnListHandler(name), params);
	}

	public List<Object> query2ColumnList(Page page, String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(this.pageTransfer(sql, page, params), new ColumnListHandler(name), params);
	}

	public List<Object> query2ColumnList(Connection con, String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new ColumnListHandler(name), params);
	}

	public List<Object> query2ColumnList(Connection con, Page page, String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, this.pageTransfer(sql, page, params), new ColumnListHandler(name), params);
	}

	public Map<Object, Map<String, Object>> query2Keyed(String sql, String key, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new KeyedHandler(key), params);
	}

	public Map<Object, Map<String, Object>> query2Keyed(Page page, String sql, String key, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(this.pageTransfer(sql, page, params), new KeyedHandler(key), params);
	}

	public Map<Object, Map<String, Object>> query2Keyed(Connection con, String sql, String key, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new KeyedHandler(key), params);
	}

	public Map<Object, Map<String, Object>> query2Keyed(Connection con, Page page, String sql, String key, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, this.pageTransfer(sql, page, params), new KeyedHandler(key), params);
	}

	public Map<String, Object> query2Map(String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new MapHandler(), params);
	}

	public Map<String, Object> query2Map(Connection con, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new MapHandler(), params);
	}

	public List<Map<String, Object>> query2MapList(String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new MapListHandler(), params);
	}

	public List<Map<String, Object>> query2MapList(Page page, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(this.pageTransfer(sql, page, params), new MapListHandler(), params);
	}

	public List<Map<String, Object>> query2MapList(Connection con, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new MapListHandler(), params);
	}

	public List<Map<String, Object>> query2MapList(Connection con, Page page, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, this.pageTransfer(sql, page, params), new MapListHandler(), params);
	}

	public Object query2Scalar(String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.query(sql, new ScalarHandler(name), params);
	}

	public Object query2Scalar(Connection con, String sql, String name, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.query(con, sql, new ScalarHandler(name), params);
	}

	public int update(String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner(this.ds);
		return qr.update(sql, params);
	}

	public int update(Connection con, String sql, Object[] params) throws SQLException {
		QueryRunner qr = new QueryRunner();
		return qr.update(con, sql, params);
	}

	public abstract String pageTransfer(String sql, Page page, Object[] vs) throws SQLException;

	@Override
	public int update(Object o ,Map<String ,Object> request) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getUpdateSQL(o, values ,request);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(sql, values.toArray());
	}
	@Override
	public int update(Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getUpdateSQL(o, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(sql, values.toArray());
	}
	@Override
	public int delete(Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getDeleteSQL(o, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(sql, values.toArray());
	}

	@Override
	public int update(Connection con, Object o ,Map<String ,Object> request) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getUpdateSQL(o, values ,request);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(con, sql, values.toArray());
	}
	
	
	@Override
	public int update(Connection con, Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getUpdateSQL(o, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(con, sql, values.toArray());
	}

	@Override
	public int delete(Connection con, Object o) throws Exception {
		List<Object> values = new ArrayList<Object>();
		String sql = Utils.getDeleteSQL(o, values);
		if (log.isDebugEnabled()) {
			log.debug(sql);
		}
		return this.update(con, sql, values.toArray());
	}

	@Override
	public int updateLOB(String sql, Object[] objs) throws SQLException, IOException {
		return this.update(this.ds.getConnection(), sql, objs);
	}

	@Override
	public int updateLOB(Connection con, String sql, Object[] objs) throws SQLException, IOException {

		PreparedStatement ps = null;
		ps = con.prepareStatement(sql);
		int i = 1;
		for (Object o : objs) {
			if (o instanceof InputStream) {
				ps.setBinaryStream(i, (InputStream) o, ((InputStream) o).available());
			} else if (o instanceof Integer) {
				ps.setInt(i, (Integer) o);
			} else if (o instanceof Double) {
				ps.setDouble(i, (Double) o);
			} else if (o instanceof Boolean) {
				ps.setBoolean(i, (Boolean) o);
			} else if (o instanceof BigDecimal) {
				ps.setBigDecimal(i, (BigDecimal) o);
			} else if (o instanceof Long) {
				ps.setLong(i, (Long) o);
			} else if (o instanceof Time) {
				ps.setTime(i, (Time) o);
			} else if (o instanceof Timestamp) {
				ps.setTimestamp(i, (Timestamp) o);
			} else if (o instanceof String) {
				ps.setString(i, (String) o);
			} else {
				ps.setObject(i, o);
			}
			++i;
		}
		return ps.executeUpdate();
	}

}
