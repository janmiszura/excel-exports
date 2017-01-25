package com.gleidsonfersanp.db.query;

public class ExportColumnQuery {

	private String columnName;
	private String columnAlias;

	public ExportColumnQuery(String columnName, String columnAlias) {
		this.columnName = columnName;
		this.columnAlias = columnAlias;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnAlias() {
		return columnAlias;
	}
	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}
}
