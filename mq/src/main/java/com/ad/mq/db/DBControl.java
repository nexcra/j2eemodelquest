package com.ad.mq.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 操作数据库
 * 
 * @author YMQ
 * 
 */
public interface DBControl {
	/**
	 * 设置DataSource
	 * 
	 * @param ds
	 */
	void setDataSource(DataSource ds);

	/**
	 * 获取DataSource
	 * 
	 * @return
	 */
	DataSource getDataSource();

	/**
	 * 查询出字段元数据
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public ResultSetMetaData query2ResultSetMetaData(String sql) throws SQLException, IOException ;

	/**
	 * 查询出字段元数据
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public ResultSetMetaData query2ResultSetMetaData(Connection con, String sql) throws SQLException, IOException ;

	/**
	 * 查询出字段元数据
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public ResultSetMetaData query2ResultSetMetaData(Connection con, String sql, Object[] params) throws SQLException, IOException ;

	/**
	 * 把结果集中的第一行数据转成对象数组
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Object[] query2Array(String sql, Object[] params) throws SQLException;

	/**
	 * 把结果集中的第一行数据转成对象数组
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Object[] query2Array(Connection con, String sql, Object[] params) throws SQLException;

	/**
	 * 把结果集中的每一行数据都转成一个对象数组，再存放到List中
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Object[]> query2ArrayList(String sql, Object[] params) throws SQLException;

	/**
	 * 把结果集中的每一行数据都转成一个对象数组，再存放到List中
	 * 
	 * @param page
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Object[]> query2ArrayList(Page page, String sql, Object[] params) throws SQLException;

	/**
	 * 把结果集中的每一行数据都转成一个对象数组，再存放到List中
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Object[]> query2ArrayList(Connection con, String sql, Object[] params) throws SQLException;

	/**
	 * 把结果集中的每一行数据都转成一个对象数组，再存放到List中
	 * 
	 * @param con
	 * @param page
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Object[]> query2ArrayList(Connection con, Page page, String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的第一行数据封装到一个对应的JavaBean实例中
	 * 
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	Object query2Bean(String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中的第一行数据封装到一个对应的JavaBean实例中
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	Object query2Bean(Connection con, String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里
	 * 
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */

	List<?> query2BeanList(String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里
	 * 
	 * @param page
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	List<?> query2BeanList(Page page, String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	List<?> query2BeanList(Connection con, String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里
	 * 
	 * @param con
	 * @param page
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	List<?> query2BeanList(Connection con, Page page, String sql, Class<?> clazz, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一列的数据存放到List中
	 * 
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	List<Object> query2ColumnList(String sql, String name, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一列的数据存放到List中
	 * 
	 * @param page
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	List<Object> query2ColumnList(Page page, String sql, String name, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一列的数据存放到List中
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	List<Object> query2ColumnList(Connection con, String sql, String name, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一列的数据存放到List中
	 * 
	 * @param con
	 * @param page
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	List<Object> query2ColumnList(Connection con, Page page, String sql, String name, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里
	 * 
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<Object, Map<String, Object>> query2Keyed(String sql, String key, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里
	 * 
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<Object, Map<String, Object>> query2Keyed(Page page, String sql, String key, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<Object, Map<String, Object>> query2Keyed(Connection con, String sql, String key, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里
	 * 
	 * @param con
	 * @param page
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<Object, Map<String, Object>> query2Keyed(Connection con, Page page, String sql, String key, Object[] params) throws SQLException;

	/**
	 * 将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
	 * 
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> query2Map(String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> query2Map(Connection con, String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再存放到List。
	 * 
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> query2MapList(String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再存放到List。
	 * 
	 * @param page
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> query2MapList(Page page, String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再存放到List。
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> query2MapList(Connection con, String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中的每一行数据都封装到一个Map里，然后再存放到List。
	 * 
	 * @param con
	 * @param page
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> query2MapList(Connection con, Page page, String sql, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一条记录的其中某一列的数据存成Object。
	 * 
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 */
	Object query2Scalar(String sql, String name, Object[] params) throws SQLException;

	/**
	 * 将结果集中某一条记录的其中某一列的数据存成Object。
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	Object query2Scalar(Connection con, String sql, String name, Object[] params) throws SQLException;

	/**
	 * 新增/修改数据
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int update(String sql, Object[] params) throws SQLException;

	/**
	 * 新增/修改数据
	 * 
	 * @param con
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int update(Connection con, String sql, Object[] params) throws SQLException;

	/**
	 * 实体类的Update
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	int update(Object o) throws Exception;

	/**
	 * 实体类的Update
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	int update(Object o, Map<String, Object> request) throws Exception;

	/**
	 * 实体类的Update
	 * 
	 * @param con
	 * @param o
	 * @return
	 * @throws Exception
	 */
	int update(Connection con, Object o) throws Exception;

	/**
	 * 实体类的Update
	 * 
	 * @param con
	 * @param o
	 * @return
	 * @throws Exception
	 */
	int update(Connection con, Object o, Map<String, Object> request) throws Exception;

	/**
	 * 实体类的Insert
	 * 
	 * @param o
	 * @return
	 */
	Map<String, Object> insert(Object o) throws Exception;

	/**
	 * 实体类的Insert
	 * 
	 * @param con
	 * @param o
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> insert(Connection con, Object o) throws Exception;

	/**
	 * 实体类的Delete
	 * 
	 * @param o
	 * @return
	 */
	int delete(Object o) throws Exception;

	/**
	 * 实体类的Delete
	 * 
	 * @param con
	 * @param o
	 * @return
	 * @throws Exception
	 */
	int delete(Connection con, Object o) throws Exception;

	/**
	 * 更新大对象
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 * @throws SQLException
	 */
	int updateLOB(String sql, Object[] objs) throws SQLException, IOException;

	/**
	 * 更新大对象
	 * 
	 * @param con
	 * @param sql
	 * @param objs
	 * @return
	 * @throws SQLException
	 */
	int updateLOB(Connection con, String sql, Object[] objs) throws SQLException, IOException;

	/**
	 * 分页转换SQL
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	String pageTransfer(String sql, Page page, Object[] vs) throws SQLException;

	/**
	 * the same of QueryRunner
	 * @param conn
	 * @param sql
	 * @param rsh
	 * @return
	 */
	<T> T query(Connection conn, String sql, ResultSetHandler<T> rsh);

	/**
	 * the same of QueryRunner
	 * @param conn
	 * @param sql
	 * @param rsh
	 * @param params
	 * @return
	 */
	<T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params);

	/**
	 * the same of QueryRunner
	 * @param sql
	 * @param params
	 * @param rsh
	 * @return
	 */
	<T> T query(String sql, Object[] params, ResultSetHandler<T> rsh);

	/**
	 * the same of QueryRunner
	 * @param sql
	 * @param param
	 * @param rsh
	 * @return
	 */
	<T> T query(String sql, Object param, ResultSetHandler<T> rsh);

	/**
	 * the same of QueryRunner
	 * @param sql
	 * @param rsh
	 * @return
	 */
	<T> T query(String sql, ResultSetHandler<T> rsh);

	/**
	 * the same of QueryRunner
	 * @param sql
	 * @param rsh
	 * @param params
	 * @return
	 */
	<T> T query(String sql, ResultSetHandler<T> rsh, Object... params);
}
