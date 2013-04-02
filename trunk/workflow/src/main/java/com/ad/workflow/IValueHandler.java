package com.ad.workflow;

import java.sql.Connection;
import java.sql.SQLException;

import com.ad.mq.db.DBControl;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.view.VWorkFlowDocument;

public interface IValueHandler {
	Object getValue(Connection conn ,VWorkFlowDocument document ,WorkFlowDocumentStep step,String cfg) throws SQLException;
	void setDBCtl(DBControl db);
}
