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
			}else{
				srcClmn.setLabelname(rsmda.getColumnName(i+1));
				srcClmn.setClmnname(rsmda.getColumnName(i+1));
				srcClmn.setDatatype(rsmda.getColumnTypeName(i+1));
			}
			this.db.insert(srcClmn);
		}
	}
}
