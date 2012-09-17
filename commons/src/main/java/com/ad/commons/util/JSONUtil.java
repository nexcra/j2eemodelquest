package com.ad.commons.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.ad.commons.cfg.App;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JSONUtil implements JsonValueProcessor {

	private DateFormat dateFormat;
	private String pattern = null;

	public JSONUtil(){
		
	}
	public JSONUtil(String datePattern) {
		pattern = datePattern;
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if (value == null) {
			return "";
		}
		String pn;

		if (value instanceof Date) {
			pn = (StringUtils.isEmpty(this.pattern)) ? App.DEFAULT_DATE_PATTERN : this.pattern;
			dateFormat = new SimpleDateFormat(pn);
			return dateFormat.format((Date) value);
		}
		if (value instanceof Timestamp) {
			pn = (StringUtils.isEmpty(this.pattern)) ? App.DEFAULT_TIMESTAMP_PATTERN : this.pattern;
			dateFormat = new SimpleDateFormat(pn);
			return dateFormat.format((Timestamp) value);
		}

		if (value instanceof Time) {
			pn = (StringUtils.isEmpty(this.pattern)) ? App.DEFAULT_TIME_PATTERN : this.pattern;
			dateFormat = new SimpleDateFormat(pn);
			return dateFormat.format((Time) value);
		}

		return "";

	}
}
