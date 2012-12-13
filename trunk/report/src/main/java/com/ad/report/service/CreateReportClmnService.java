package com.ad.report.service;

import java.sql.ResultSetMetaData;
import java.util.Map;

import com.ad.mq.db.DBControl;
import com.ad.report.model.ReportSrc;
import com.ad.report.model.ReportSrcClmn;

public class CreateReportClmnService {
	private DBControl db;

	public void setDb(DBControl db) {
		this.db = db;
	}

	public void syncReportClmn(ReportSrc reportSrc) throws Exception {
		Map<Object, Map<String, Object>> clmns = this.db.query2Keyed("Select * From REPORT$SRC_CLMN where srcid=?", "CLMNNAME", new Object[] { reportSrc.getId() });
		System.out.println(clmns);
		ResultSetMetaData rsmda = this.db.query2ResultSetMetaData(reportSrc.getSql());
		Map<String ,Object> data;
		ReportSrcClmn srcClmn ;
		this.db.update("Delete From REPORT$SRC_CLMN where srcid=?", new Object[]{reportSrc.getId()});
		for(int i=0,len=rsmda.getColumnCount() ;i<len ;i++){
			data = clmns.get(rsmda.getColumnName(i+1));
			srcClmn = new ReportSrcClmn();
			srcClmn.setSrcid(reportSrc.getId());
			if (data != null){
				
				srcClmn.setLabelname(data.get("LABELNAME").toString());
				srcClmn.setClmnname(data.get("CLMNNAME").toString());
				srcClmn.setDatatype(data.get("DATATYPE").toString());
//				srcClmn.setSrcid(Integer.parseInt(data.get("SRCID").toString()));
//				data.put("DATATYPE", rsmda.getColumnTypeName(i+1));
			}else{
				srcClmn.setLabelname(rsmda.getColumnName(i+1));
				srcClmn.setClmnname(rsmda.getColumnName(i+1));
				srcClmn.setDatatype(rsmda.getColumnTypeName(i+1));
//				srcClmn.setSrcid(Integer.parseInt(data.get("SRCID").toString()));
//				data = new Hashtable<String ,Object>();
//				data.put("SRCID", reportSrc.getId());//{SRCID=100, DATATYPE=7, ID=200, LABELNAME=456, CLMNNAME=123}}
//				data.put("DATATYPE", rsmda.getColumnTypeName(i+1));
//				data.put("LABELNAME", rsmda.getColumnTypeName(i+1));
//				data.put("CLMNNAME", rsmda.getColumnTypeName(i+1));
			}
			this.db.insert(srcClmn);
			
//			System.out.println("getColumnName:" + rsmda.getColumnName(i+1));
//			System.out.println("getColumnLabel:" +rsmda.getColumnLabel(i+1));
//			System.out.println("getColumnClassName:" +rsmda.getColumnClassName(i+1));
//			System.out.println("getCatalogName:" +rsmda.getCatalogName(i+1));
//			System.out.println("getColumnDisplaySize:" +rsmda.getColumnDisplaySize(i+1));
//			System.out.println("getColumnType:" +rsmda.getColumnType(i+1));
//			System.out.println("getColumnTypeName:" +rsmda.getColumnTypeName(i+1));
//			System.out.println("getPrecision:" +rsmda.getPrecision(i+1));
//			System.out.println("getScale:" +rsmda.getScale(i+1));
//			System.out.println("getSchemaName:" +rsmda.getSchemaName(i+1));
//			System.out.println("getTableName:" +rsmda.getTableName(i+1));
//			System.out.println("--------------------------------------------");
		}
	}
}
