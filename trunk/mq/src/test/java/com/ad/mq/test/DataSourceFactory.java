package com.ad.mq.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

public class DataSourceFactory {
	public static DataSource getDataSource() throws SQLException {
		OracleDataSource ods = new OracleDataSource();
		ods.setDriverType("thin"); // type of driver
		ods.setNetworkProtocol("tcp"); // tcp is the default anyway
		ods.setServerName("192.168.1.221"); // database server name
		ods.setDatabaseName("wilson"); // Oracle SID
		ods.setPortNumber(1521); // listener port number
		ods.setUser("add_oa"); // username
		ods.setPassword("add_oa"); // password
		return ods;
	}
}
