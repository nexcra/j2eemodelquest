package com.ad.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Utils {

	/**
	 * 将字符串拆分成INodeHandler对象列表
	 * @param str
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static List<INodeHandler> convertString2Handler(String str) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		String[] arr = StringUtils.split(str);
		List<INodeHandler> handlers = new ArrayList<INodeHandler>();
		for (String s : arr) {
			Object o = Class.forName(s).newInstance();
			if (o instanceof INodeHandler) {
				handlers.add((INodeHandler) o);
			}
		}
		return handlers;
	}
}
