package com.ad.workflow.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.IUser;
import com.ad.workflow.INodeHandler;
import com.ad.workflow.IWorkFlow;
import com.ad.workflow.model.WorkFlowDocument;
import com.ad.workflow.model.WorkFlowNode;
import com.ad.workflow.model.WorkFlowTransition;
import com.ad.workflow.model.view.VWorkFlowDocument;

public class DefaultWorkFlowService implements IWorkFlow {

	private DBControl db;

	private String SQL = "SELECT a.ID, a.WFID, a.CREATETIME, a.ENDTIME, a.STATUS, "
									+ "       b.name wfname,"
									+ "      c.name nodename,"
									+ "      d.USERNAME usrname,"
									+ "      c.TYPE nodetype,"
									+ "      e.id AS stepid,"
									+ "      e.enterdate,"
									+ "       e.msg,"
									+ "       e.NID, "
									+ "       e.USRID"
									+ " FROM WORKFLOW$DOCUMENT a,"
									+ "      workflow b,"
									+ "      workflow$node c,"
									+ "       usertable d,"
									+ "       workflow$document$steps e"
									+ " WHERE     a.wfid = b.id       "
									+ "       AND a.id = e.did"
									+ "       AND e.nid = c.id"
									+ "       AND e.status = 0"
									+ "      AND a.status = 1"
									+ "      AND e.usrid = d.userid"
									+ "       AND a.ENDTIME IS NULL AND a.id=?";

	public void setDb(DBControl db) {
		this.db = db;
	}

	/**
	 * 工作流开始
	 */
	@Override
	public VWorkFlowDocument Start(Connection conn, Integer wfid, IUser usr) throws Exception {

		WorkFlowNode node = (WorkFlowNode) this.db.query2Bean(conn, " select * from WORKFLOW$NODE where wfid = ? and type=?", WorkFlowNode.class, new Object[] { wfid, IWorkFlow.NODE_START });

		WorkFlowDocument doc = new WorkFlowDocument();
		VWorkFlowDocument document = new VWorkFlowDocument();

		//doc.setUsrid(usr.getUserId());
		doc.setCreatetime(new Timestamp(System.currentTimeMillis()));
		doc.setWfid(wfid);
		doc.setStatus(true);
		//doc.setNid(node.getId());

		//document.setUsrid(doc.getUsrid());
		document.setCreatetime(doc.getCreatetime());
		document.setWfid(doc.getWfid());
		document.setStatus(doc.getStatus());
		//document.setNid(node.getId());

		Object nodeHandler = Class.forName(node.getHandler()).newInstance();
		if (nodeHandler instanceof DataBaseAware) {
			((DataBaseAware) nodeHandler).setDBControl(this.db);
		}

		if (nodeHandler instanceof INodeHandler) {
			((INodeHandler) nodeHandler).beforeEnter(null, node, document, conn, null, usr);
		} else {
			new RuntimeException(node.getHandler() + "不是INodeHandler的实现类！");
		}

		Map<String, Object> rst = this.db.insert(conn, doc);
		Integer did = Integer.parseInt(rst.get("id").toString());
		doc.setId(did);
		document.setId(doc.getId());

		((INodeHandler) nodeHandler).enter(null, node, document, conn, null, usr);

		document = (VWorkFlowDocument) this.db.query2Bean(conn, SQL, VWorkFlowDocument.class, new Object[] { doc.getId() });

		return document;
	}

	/**
	 * 获取当前节点对应的所有Transition
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowTransition> getTransitionsByNode(Connection conn, Integer nid) throws Exception {
		return (List<WorkFlowTransition>) this.db.query2BeanList(conn, " select * from WORKFLOW$TRANSITION where fromnode =? order by id", WorkFlowTransition.class, new Object[] { nid });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowTransition> getTransitionsByDocument(Connection conn, Integer did) throws Exception {
		return (List<WorkFlowTransition>) this.db.query2BeanList(conn, "select a.* from WORKFLOW$TRANSITION a,workFlow_document b where a.fromnode = b.nid and b.id=? order by a.id",
				WorkFlowTransition.class, new Object[] { did });

	}

}
