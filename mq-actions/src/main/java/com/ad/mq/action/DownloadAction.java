package com.ad.mq.action;

import java.io.BufferedInputStream;
import java.sql.Blob;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.interceptor.ServletRequestAware;
import com.ad.mq.interceptor.ServletResponseAware;

public class DownloadAction extends SelectAction implements DataBaseAware, ServletResponseAware, ServletRequestAware {

	private HttpServletResponse response;
	private HttpServletRequest request;

	private String column;
	private String nameclmn;
	private String typeclmn;

	public void setColumn(String column) {
		this.column = column;
	}

	public void setNameclmn(String nameclmn) {
		this.nameclmn = nameclmn;
	}

	public void setTypeclmn(String typeclmn) {
		this.typeclmn = typeclmn;
	}

	@Override
	public void execute() throws Exception {
		putSQL();
		Map<String, Object> o = this.db.query2Map(this.sql, datas.toArray());

		if (null != o.get(this.column) && o.get(this.column) instanceof Blob) {
			Blob blob = (Blob) o.get(this.column);
			String type = (StringUtils.isEmpty(this.typeclmn) || o.get(this.typeclmn) == null) ? "application/x-msdownload" : o.get(this.typeclmn).toString();

			response.setContentType(type);
			String name = o.get(this.nameclmn) == null ? "unkown" : o.get(this.nameclmn).toString();
			name = new String(name.getBytes(), "iso8859-1");
			if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
				// IE5.5特别处理
				response.setHeader("Content-Disposition", "filename=" + name);
			} else {
				// 其它的Header设定方式
				response.addHeader("Content-Disposition", "attachment;filename=" + name);
			}
			BufferedInputStream buffer = new BufferedInputStream(blob.getBinaryStream());
			ServletOutputStream out = this.response.getOutputStream();
			byte[] bytes = new byte[1024];
			while (buffer.read(bytes) != -1) {
				out.write(bytes);
			}
			out.flush();
			this.response.setStatus(HttpServletResponse.SC_OK);
			this.response.flushBuffer();
		}
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
