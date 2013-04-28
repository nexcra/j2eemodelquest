package com.ad.workflow.impl;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.mq.model.IUser;
import com.ad.workflow.model.WorkFlowDocumentStep;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultTaskNodePoolHandler extends NodeHandlerAdapter {
	private Logger log = Logger.getLogger(DefaultTaskNodePoolHandler.class);
	
	@Override
	public WorkFlowDocumentStep enter(Integer fromnode, WorkFlowNode node, VWorkFlowDocument document, Connection conn, Integer sid, IUser usr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enter invoke!");
		}

		if (node.getId() < fromnode){ //回退
			
		}else{
			String sql = node.getCfg();
			sql =StringUtils.replaceEachRepeatedly(sql, new String[]{":did",":nid",":sid"}, new String[]{document.getId().toString() , node.getId().toString() ,sid.toString()});
			Object[] rst = this.db.query2Array(conn ,sql ,new Object[]{});
			if (null == rst || rst.length ==0){
				throw new Exception("多人审批节点，没有找到条件审批人！");
			}
			node.setUsrid(Integer.parseInt(rst[0].toString()));
		}
		
		//this.db.update(conn, "update WORKFLOW$DOCUMENT set usrid = ? ,nid = ? where id =?", new Object[] { node.getUsrid(), node.getId(), document.getId() });
		// WorkFlowDocumentStep step = new WorkFlowDocumentStep();
		// step.setDid(document.getId());
		// step.setEnterdate(new Timestamp(System.currentTimeMillis()));
		// step.setNid(node.getId());
		// step.setUsrid(node.getUsrid());
		// step.setStatus(IWorkFlow.STEP_WORKING);
		// step.setFromnid(fromnode);
		// step.setFromsid(sid);
		// this.db.insert(conn, step);
		return super.enter(fromnode, node, document, conn, sid, usr);
	}
}
