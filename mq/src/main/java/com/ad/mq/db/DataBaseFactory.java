package com.ad.mq.db;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import com.ad.mq.db.impl.Oracle;
/**
 * 数据库访问工厂
 * @author YMQ
 *
 */
public class DataBaseFactory {
	
	public static DBControl getDBControl(String name) {
		DBControl dbc = null;
		if (name.equalsIgnoreCase("oracle")) {
			dbc = new Oracle();
		}
		return dbc;
	}

	public static DBControl getDBControl(String name, String dsName) throws Exception {
		DBControl dbc = null;
		if (name.equalsIgnoreCase("oracle")) {
			dbc = new Oracle();
		}
		if (null != dbc){
			dbc.setDataSource(getDataSource(dsName));
		}
		return dbc;
	}

	/**
	 * 获取JNDI
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public static  DataSource getDataSource(String name) throws Exception {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/" + name);
			if (ds == null)
				throw new Exception(name + " is an unknown DataSource");
			return ds;

	}
}
