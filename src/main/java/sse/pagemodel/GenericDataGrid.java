package sse.pagemodel;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 数据列表模型定义
 * @author fengking (http://fengkinglbs.duapp.com/)
 * @version 1.0
 * @created 2014-10-1 
 *
 * @param <T> 数据模型
 */
public class GenericDataGrid<T> {
	private int total = 0;
	private List<T> rows = new ArrayList<T>();

	/**
	 * 构造函数
	 */
	public GenericDataGrid() {
		super();
	}

	/**
	 * 返回列表数据
	 * @param total 总行数
	 * @param rows	行数据列表
	 */
	public GenericDataGrid(int total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	/**
	 * 返回总记录数
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置总记录数
	 * @param total
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 返回总行数列表
	 * @return
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置总行数列表
	 * @param rows
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
