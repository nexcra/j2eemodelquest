package com.ad.report.action;

import com.ad.mq.action.InsertAction;
import com.ad.mq.model.IUser;
import com.ad.mq.model.OutData;
import com.ad.report.model.ReportObj;

public class SaveReportPlanAction extends InsertAction {
	private String name;
	private Integer srcid;
	private String rptcfg;
	private Boolean ispublic;

	public void setName(String name) {
		this.name = name;
	}

	public void setSrcid(Integer srcid) {
		this.srcid = srcid;
	}

	public void setIspublic(Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public void setRptcfg(String rptcfg) {
		this.rptcfg = rptcfg;
	}

	@Override
	public void execute() throws Exception {
		this.out =new OutData();
		IUser user = (IUser) this.session.get("user");
		ReportObj obj = new ReportObj();
		obj.setCfg(rptcfg);
		obj.setIspublic(ispublic);
		obj.setName(name);
		obj.setUsrid(user.getUserId());
		obj.setSrcid(srcid);
		this.db.insert(obj);
	}

}
