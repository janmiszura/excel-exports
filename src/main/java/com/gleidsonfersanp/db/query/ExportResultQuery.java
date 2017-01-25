package com.gleidsonfersanp.db.query;

import java.util.List;

public class ExportResultQuery {

	private List<ExportColumnResult>columnResults;

	public ExportResultQuery(List<ExportColumnResult> columnResults) {
		this.columnResults = columnResults;
	}

	public List<ExportColumnResult> getColumnResults() {
		return columnResults;
	}

	public void setColumnResults(List<ExportColumnResult> columnResults) {
		this.columnResults = columnResults;
	}

}
