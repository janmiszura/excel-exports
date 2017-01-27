package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.extra.exception.GeneralException;

public interface IExcelExports {

	File writeFileForLocalPath(ExportQuery exportQuery, String path, String fileName) throws IOException, SQLException, GeneralException;
	File writeFileForLocalPath(String sql, String path, String fileName) throws IOException, SQLException, GeneralException;
	void writeFileForOutputStream(ExportQuery exportQuery, String fileName, OutputStream os) throws IOException, SQLException, GeneralException;
	void writeFileForOutputStream(String sql, String fileName, OutputStream os) throws IOException, SQLException, GeneralException;

}
