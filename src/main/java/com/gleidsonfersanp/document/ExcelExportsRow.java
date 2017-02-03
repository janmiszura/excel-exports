package com.gleidsonfersanp.document;

import java.util.ArrayList;
import java.util.List;

public class ExcelExportsRow {

	private Integer number;
	private List<ExcelExportsCell> cells = new ArrayList<ExcelExportsCell>();

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public List<ExcelExportsCell> getCells() {
		return cells;
	}
	public void setCells(List<ExcelExportsCell> cells) {
		this.cells = cells;
	}
}
