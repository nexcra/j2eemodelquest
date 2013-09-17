package com.ad.mq.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.mq.db.Page;
import com.ad.mq.interceptor.PagingAware;
import com.ad.mq.model.DataElement;
import com.ad.mq.model.OutData;

/**
 * 根据数据源查询数据可以分页 分页需要传入: limit:每页行数 start:第几页
 * 
 * @author YMQ
 * 
 */
public class SelectPageAction extends SelectAction implements PagingAware {

	private final Logger log = Logger.getLogger(SelectPageAction.class);
	private Page page;

	private static final String REGX = "\\s*summaryType\\s*:\\s*['|\"].*?['|\"]";

	@Override
	public void execute() throws Exception {
		OutData od = new OutData();
		this.out = od;

		putSQL();
		if (null!= this.$summary && this.$summary == 1) {
			// -- count，sum，min，max，average
			@SuppressWarnings("unchecked")
			List<DataElement> eles = (List<DataElement>) this.db.query2BeanList("select * from mq$element where parentid =? and columnattrs like '%summaryType%'", DataElement.class,
					new Object[] { this.$dataid });
			StringBuffer sb = new StringBuffer();
			String type = null;
			for (DataElement ele : eles) {
				if (!StringUtils.isEmpty(ele.getColumnattrs())) {
					Pattern p = Pattern.compile(REGX);
					Matcher m = p.matcher(ele.getColumnattrs());
					while (m.find()) {
						type = m.group();
					}
					type = type.split(":")[1].replaceAll("['|\"]", "");
					sb.append(type).append("(").append(ele.getFieldvalue()).append(") AS ").append(ele.getFieldvalue()).append(",");
				}
			}
			if (sb.length() > 0) {
				Map<String, Object> summary = this.db.query2Map("SELECT " + sb.toString() + "1 FROM (" + this.sql + ")", datas.toArray());
				List<Map<String, Object>> summaryList = new ArrayList<Map<String, Object>>();
				summaryList.add(summary);
				od.setSummaryData(summaryList);
			}
			// +++
		}

		List<?> list = this.db.query2BeanList(this.page, this.sql, clzz, datas.toArray());

		// od.setCfg(this.dataCfg);
		od.setData(list);
		od.setTotal(this.page.getTotal());

		if (log.isDebugEnabled()) {
			log.debug("page.start=" + this.page.getStartIndex());
			log.debug("page.end=" + this.page.getEndIndex());
		}
	}

	@Override
	public void setPage(Page page) {
		this.page = page;
	}

}
