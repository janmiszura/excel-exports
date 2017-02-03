package com.gleidsonfersanp.document;

import java.util.List;

public class ExcelExportsDocumentBuilder {

	private ExcelExportsDocument document;

	public ExcelExportsDocumentBuilder row(ExcelExportsRow row){
		this.document.getRows().add(row);
		return this;
	}

	public ExcelExportsDocumentBuilder rowa(List<ExcelExportsRow> rows){
		this.document.getRows().addAll(rows);
		return this;
	}

	public ExcelExportsDocument build(){
		return this.document;
	}

}
