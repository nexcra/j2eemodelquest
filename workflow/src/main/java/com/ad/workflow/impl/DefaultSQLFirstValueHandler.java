package com.ad.workflow.impl;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.ad.mq.db.DBControl;
import com.ad.workflow.IValueHandler;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultSQLFirstValueHandler implements IValueHandler {

	private DBControl db;
	@Override
	public Object getValue(VWorkFlowDocument document, WorkFlowDocumentStep step, String cfg) throws SQLException {
		String sql = StringUtils.replace(cfg, ":did", document.getId() + "");
		Object[] rst = this.db.query2Array(sql, new Object[]{});
		return rst[0];
	}

	@Override
	public void setDBCtl(DBControl db) {
		this.db = db;
	}

}
