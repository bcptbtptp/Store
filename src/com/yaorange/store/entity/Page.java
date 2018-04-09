package com.yaorange.store.entity;

import java.util.List;

/**
 *分页
 *
 */
public class Page {
	private List<?> list;
	
	private Integer currPage;//当前页
	private Integer totalPage;//总页数
	private Integer pageSize=12;//每页显示多少条	
	private Integer totalCount;//总记录数
	private Integer beginRows;//起始数据数
	
	
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Integer getCurrPage() {
		if(currPage<=0)
		{
			currPage = 1;
		}
		return currPage;
	}
	public void setCurrPage(Integer currPage) {
		if(currPage==null)
		{
			currPage=1;
		}
		if(currPage>getTotalPage())
		{
			currPage = getTotalPage();
		}
			
		this.currPage = currPage;
	}
	

	public Integer getTotalPage() {
		Double d = (double)totalCount/(double)pageSize;
		totalPage=(int) Math.ceil(d);
		
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getBeginRows() {
		 beginRows = (this.getCurrPage()-1)*this.getPageSize();
		return beginRows;
	}
	
	
	
}
