package com.ad.mq.model;

import java.io.Serializable;

/**
 * 输出数据
 * 
 * @author YMQ
 * 
 */
public class OutData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean success = true;
	private String message = "";
	private Object data;
	private Boolean session = true;
	private Long total = 0L;
	private String cfg ="{}" ;
	private Integer auth = 0;
	private Integer token = 0;
	
	
	
	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

	/**
	 * 配制
	 * @return
	 */
	public String getCfg() {
		return cfg;
	}

	/**
	 * 配制
	 * @param cfg
	 */
	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

	/**
	 * 总记录数
	 * @return
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * 总记录数
	 * @param total
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * 登录状态
	 * 
	 * @return
	 */
	public Boolean getSession() {
		return session;
	}

	/**
	 * 登录状态
	 * 
	 * @param session
	 */
	public void setSession(Boolean session) {
		this.session = session;
	}

	/**
	 * 成功执行完成
	 * @return
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * 成功执行完成
	 * @param success
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * 一般信息
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 一般信息
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 具体数据
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 具体数据
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
