package com.ad.mq.interceptor;

import java.util.Collection;

import javax.servlet.http.Part;

public interface MultipartAware {
	void setParts(Collection<Part> parts);
}
