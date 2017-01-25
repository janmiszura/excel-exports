package com.gleidsonfersanp.db.query;

import java.util.List;

import com.gleidsonfersanp.extra.Util;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExportQueryBuilder {

	private ExportQuery instance;

	public ExportQueryBuilder() {
		this.instance = new ExportQuery();
	}

	public ExportQueryBuilder table(String table){
		this.instance.setTable(table);
		return this;
	}

	public ExportQueryBuilder columnQueries(ExportColumnQuery columnQuery){
		this.instance.getColumnQuerys().add(columnQuery);
		return this;
	}

	public ExportQueryBuilder columnsQueries(List<ExportColumnQuery> columnsQueries){

		for (ExportColumnQuery exportColumnQuery : columnsQueries) {
			this.instance.getColumnQuerys().add(exportColumnQuery);
		}

		return this;
	}

	public ExportQueryBuilder firstResult(int firstResult){
		this.instance.setFirstResult(firstResult);
		return this;
	}

	public ExportQueryBuilder maxResult(int maxResult){
		this.instance.setMaxResult(maxResult);
		return this;
	}

	public ExportQuery build() throws GeneralException {
		validate();
		return this.instance;
	}

	private void validate() throws GeneralException {
		if(Util.isNullOrEmpty(this.instance.getTable()))
			throw new GeneralException("Please insert data base name");
	}

}
