package com.gleidsonfersanp.db.connection;

import com.gleidsonfersanp.extra.Util;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class EXEXDataSourceBuilder {

	private EXEXDataSource instance;

	public EXEXDataSourceBuilder() {
		this.instance = new EXEXDataSource();
	}

	public EXEXDataSourceBuilder dataBase(DataBase dataBase){
		this.instance.setDataBase(dataBase);
		return this;
	}

	public EXEXDataSourceBuilder url(String url){
		this.instance.setUrl(url);
		return this;
	}

	public EXEXDataSourceBuilder user(String user){
		this.instance.setUser(user);
		return this;
	}
	public EXEXDataSourceBuilder password(String password){
		this.instance.setPassword(password);
		return this;
	}

	public EXEXDataSource build() throws GeneralException{
		this.validate();
		return this.instance;
	}

	private void validate() throws GeneralException{
		if(this.instance.getDataBase() == null)
			throw new GeneralException("Please insert data base name");
		if(Util.isNullOrEmpty(this.instance.getUrl()))
			throw new GeneralException("Please insert data base url");
		if(Util.isNullOrEmpty(this.instance.getUser()))
			throw new GeneralException("Please insert data base user");
		if(Util.isNullOrEmpty(this.instance.getPassword()))
			throw new GeneralException("Please insert data base password");
	}

}
