package com.gleidsonfersanp.document;

import java.util.List;

public class ExcelExportsRowBuilder {

	private ExcelExportsRow instance;

	public ExcelExportsRowBuilder number(Integer number) {
		this.instance.setNumber(number);
		return this;
	}

	public ExcelExportsRowBuilder cell(ExcelExportsCell cell) {
		this.instance.getCells().add(cell);
		return this;
	}

	public ExcelExportsRowBuilder cells(List<ExcelExportsCell> cells) {
		this.instance.getCells().addAll(cells);
		return this;
	}

	public ExcelExportsRow build() {
		return this.instance;
	}

}
