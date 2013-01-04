package com.ad.report.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.OutData;
import com.ad.report.model.ReportSrc;

public class QueryReportAction extends ActionSupport implements DataBaseAware {

	private DBControl db;
	private String reportcfg;

	public void setReportcfg(String reportcfg) {
		this.reportcfg = reportcfg;
	}

	@Override
	public void setDBControl(DBControl ctl) {
		this.db = ctl;

	}

	@Override
	public void execute() throws Exception {
		OutData od = new OutData();
		Map<String, Object> rstData = new HashMap<String, Object>();
		this.out = od;
		StringBuffer colclmns = new StringBuffer();
		StringBuffer rowclmns = new StringBuffer();
		StringBuffer groupclmns = new StringBuffer();
		StringBuffer whereclmns = new StringBuffer();

		StringBuffer newsql;

		JSONObject rpcfg = this.json2object(this.reportcfg);
		Integer rpdataid = rpcfg.getInt("dataid");

		ReportSrc rs = (ReportSrc) this.db.query2Bean("select * from REPORT$SRC where id=?", ReportSrc.class, new Object[] { rpdataid });
		if (null == rs) {
			od.setMessage("ReportSrc.id=" + rpdataid + " is undefined!");
			return;
		}
		JSONArray colsobj = rpcfg.getJSONArray("cols");
		JSONObject clmn;
		for (int i = 0, len = colsobj.size(); i < len; i++) {
			clmn = JSONObject.fromObject(colsobj.get(i));
			colclmns.append("trim(").append(clmn.get("clmnname")).append("),");
		}

		JSONArray rowsobj = rpcfg.getJSONArray("rows");
		for (int i = 0, len = rowsobj.size(); i < len; i++) {
			clmn = JSONObject.fromObject(rowsobj.get(i));
			rowclmns.append("trim(").append(clmn.get("clmnname")).append("),");
		}

		JSONArray groupsobj = rpcfg.getJSONArray("groups");
		for (int i = 0, len = groupsobj.size(); i < len; i++) {
			if ("NUMBER".equalsIgnoreCase(JSONObject.fromObject(groupsobj.get(i)).get("datatype").toString())) {
				groupclmns.append("NVL(SUM(");
			} else {
				groupclmns.append("NVL(COUNT(");
			}
			groupclmns.append(JSONObject.fromObject(groupsobj.get(i)).get("clmnname")).append("),0),");
		}

		JSONArray wheresobj = rpcfg.getJSONArray("wheres");
		String expre, value, datatype;
		for (int i = 0, len = wheresobj.size(); i < len; i++) {

			clmn = JSONObject.fromObject(wheresobj.get(i));
			whereclmns.append(" AND ").append(clmn.get("clmnname"));
			expre = clmn.get("expre").toString();
			value = clmn.get("value").toString();
			value = value.replaceAll("'", "''");
			datatype = clmn.get("datatype").toString();

			if ("为空".equalsIgnoreCase(expre)) {
				whereclmns.append(" IS NULL");
				continue;
			} else if ("非空".equalsIgnoreCase(expre)) {
				whereclmns.append(" IS NOT NULL");
				continue;
			} else if ("左模糊".equalsIgnoreCase(expre)) {
				whereclmns.append(" LIKE '%").append(value).append("'");
				continue;
			} else if ("右模糊".equalsIgnoreCase(expre)) {
				whereclmns.append(" LIKE '").append(value).append("%'");
				continue;
			} else if ("模糊".equalsIgnoreCase(expre)) {
				whereclmns.append(" LIKE '%").append(value).append("%'");
				continue;
			} else {
				whereclmns.append(expre);
				if ("NUMBER".equalsIgnoreCase(datatype)) {
					whereclmns.append(value);
				} else if ("DATE".equalsIgnoreCase(datatype)) {
					whereclmns.append("TO_DATE('").append(value).append("','yyyy-mm-dd')");
				} else {
					whereclmns.append("'").append(value).append("'");
				}
			}
		}

		if (colclmns.length() > 0) {
			newsql = new StringBuffer();
			newsql.append("SELECT DISTINCT ").append(colclmns).append("1 FROM (").append(rs.getSql()).append(") WHERE 1=1").append(whereclmns).append(" ORDER BY ").append(colclmns).append("1");
			rstData.put("cols", this.db.query2ArrayList(newsql.toString(), new Object[] {}));
		} else {
			rstData.put("cols", new Integer[] { 1 });
		}

		if (rowclmns.length() > 0) {
			newsql = new StringBuffer();
			newsql.append("SELECT DISTINCT ").append(rowclmns).append("1 FROM (").append(rs.getSql()).append(") WHERE 1=1").append(whereclmns).append(" ORDER BY ").append(rowclmns).append("1");
			rstData.put("rows", this.db.query2ArrayList(newsql.toString(), new Object[] {}));
		} else {
			rstData.put("rows", new Integer[] { 1 });
		}

		if (groupclmns.length() > 0 && (colclmns.length() > 0 || rowclmns.length() > 0)) {
			newsql = new StringBuffer();
			newsql.append(" SELECT  ").append(colclmns).append(rowclmns).append(groupclmns).append("1 FROM (").append(rs.getSql()).append(") WHERE 1=1").append(whereclmns).append(" GROUP BY ")
					.append(colclmns).append(rowclmns).append("1");
			rstData.put("datas", this.db.query2ArrayList(newsql.toString(), new Object[] {}));

		} else {
			rstData.put("datas", new Integer[] { 1 });
		}

		od.setData(rstData);
	}

	private JSONObject json2object(String data) {
		JSONObject jsonObjet = JSONObject.fromObject(data);

		return jsonObjet;
	}
}
