package com.gleidsonfersanp.db.connection;

public class EXEXDataSource {

	private DataBase dataBase;
	private String url;
	private String user;
	private String password;

	public DataBase getDataBase() {
		return dataBase;
	}
	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
