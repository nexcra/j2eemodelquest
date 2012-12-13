package com.ad.report.action;


import org.apache.commons.lang.StringUtils;

import com.ad.mq.action.ActionSupport;
import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.OutData;

public class ModifyPermissionsAction extends ActionSupport implements DataBaseAware {

	private DBControl db;
	private String parentid;
	private String nodeid;
	private String srcid;

	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	private Boolean checked;

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public void setDBControl(DBControl arg0) {
		this.db = arg0;
	}

	@Override
	public void execute() throws Exception {
		String[] sqls = StringUtils.split(this.cfg, "\n");
		Integer cunt = 0;
		if (null == this.parentid || "0".equals(this.parentid)) {
			cunt = this.db.update(sqls[0], new Object[] { this.srcid , this.nodeid });
			if (this.checked) {
				cunt = this.db.update(sqls[1], new Object[] { this.srcid , this.nodeid });
			}

		} else {
			cunt = this.db.update(sqls[2], new Object[] { this.srcid , this.nodeid });
			if (this.checked) {
				cunt = this.db.update(sqls[3], new Object[] { this.srcid , this.nodeid });
			}

		}
		OutData od = new OutData();
		this.out  = od;
		od.setMessage("ok");
		od.setTotal((long)cunt);
	}
}
