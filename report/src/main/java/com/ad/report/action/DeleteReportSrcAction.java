package com.ad.report.action;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.mq.action.DeleteAction;
import com.ad.mq.model.OutData;

public class DeleteReportSrcAction extends DeleteAction {

	private final static Logger log = Logger.getLogger(DeleteReportSrcAction.class);

	@Override
	public void execute() throws Exception {
		String id = this.request.get("id").toString();
		if (StringUtils.isEmpty(id)) {
			return;
		}

		OutData od = new OutData();
		this.out = od;
		Integer intId = Integer.parseInt(id);
		Connection conn = this.db.getDataSource().getConnection();

		try {
			this.db.update(conn, "Delete From REPORT$SRC_AUTH where srcid=?", new Object[] { intId });
			this.db.update(conn, "Delete From REPORT$SRC_CLMN where srcid=?", new Object[] { intId });
			this.db.update(conn, "Delete From REPORT$SRC where id=?", new Object[] { intId });
			od.setData(1);
			od.setMessage("ok");
		} catch (Exception e) {
			log.error(e);
			od.setMessage(e.getMessage());
			throw e;

		}

	}

}
