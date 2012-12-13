package com.ad.report.action;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ad.mq.action.InsertAction;
import com.ad.mq.model.OutData;
import com.ad.report.model.ReportSrc;
import com.ad.report.service.CreateReportClmnService;

public class SaveReportSrcAction extends InsertAction {

	private static Logger log = Logger.getLogger(SaveReportSrcAction.class);
	@Override
	public void execute() throws Exception {

		OutData data = new OutData();
		this.out = data;
		Connection con = this.db.getDataSource().getConnection();
		con.setAutoCommit(false);
		try {
			ReportSrc object = (ReportSrc) this.getObject(ReportSrc.class);
			CreateReportClmnService crcs = new CreateReportClmnService();
			crcs.setDb(this.db);
			crcs.syncReportClmn(object);
			data.setData(this.insert(this.$dataid));
			data.setMessage("ok");
			con.commit();
		} catch (Exception e) {
			con.rollback();
			log.error(e);
		}
	}

}
