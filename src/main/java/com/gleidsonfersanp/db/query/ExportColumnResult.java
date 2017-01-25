package com.gleidsonfersanp.db.query;

import java.util.ArrayList;
import java.util.List;

public class ExportColumnResult {

	private String name;

	private List<Object>objects = new ArrayList<>();

	public ExportColumnResult(String name, List<Object> objects) {
		this.name = name;
		this.objects = objects;
	}

	public ExportColumnResult(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Object> getObjects() {
		return objects;
	}
	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

}
