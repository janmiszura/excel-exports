package com.gleidsonfersanp.db.query;

import java.util.ArrayList;
import java.util.List;

public class ExportQuery {

	private String table;
	private List<ExportColumnQuery> columnQuerys = new ArrayList<ExportColumnQuery>();
	private Integer firstResult;
	private Integer maxResult;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<ExportColumnQuery> getColumnQuerys() {
		return columnQuerys;
	}
	public void setColumnQuerys(List<ExportColumnQuery> columnQuerys) {
		this.columnQuerys = columnQuerys;
	}
	public Integer getFirstResult() {
		return firstResult;
	}
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
}
