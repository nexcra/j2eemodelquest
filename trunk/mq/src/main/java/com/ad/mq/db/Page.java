package com.ad.mq.db;
/**
 * 分页
 * @author YMQ
 *
 */
public class Page {
	
	private long startIndex = 0L;
	private long endIndex = 0L;
	private int limit = 30;
	private int pageNo = 1;
	private long total = 0L;
	/**
	 * 设置开始记录
	 * @param startIndex
	 */
	public void setStartIndex(long startIndex){
		this.startIndex = startIndex;
	}
	/**
	 * 得到开始记录
	 * @return
	 */
	public long getStartIndex(){
		return this.startIndex;
	}
	
	/**
	 * 设置结束记录
	 * @param endIndex
	 */
	public void setEndIndex(long endIndex){
		this.endIndex = endIndex;
	}
	/**
	 * 得到结束记录
	 * @return
	 */
	public long getEndIndex(){
		return this.endIndex;
	}

	/**
	 * 设置每页显示记录数
	 * @param limit
	 */
	public void setLimit(int limit){
		this.limit = limit;
	}
	/**
	 * 得到每页显示记录数
	 * @return
	 */
	public int getLimit(){
		return this.limit;
	}
	
	/**
	 * 设置当前页号
	 * @param page
	 */
	public void setPageNo(int pageNo){
		this.pageNo = pageNo;
	}
	
	/**
	 * 当到当前页号
	 * @return
	 */
	public int getPageNo(){
		return this.pageNo;
	}
	
	/**
	 * 得到总的记录数
	 * @return
	 */
	public long getTotal(){
		
		return this.total;
	}
	
	public void setTotal(long total){
		this.total = total;
	}

}
