package com.ad.mq.model;

import java.io.Serializable;

/**
 * 用户基本信息接口
 * @author YMQ
 *
 */
public interface IUser extends Serializable{
	/**
	 * 用户编号
	 * @return
	 */
	public Integer getUserId();
	/**
	 * 用户名称
	 * @return
	 */
	public String getName();
}
