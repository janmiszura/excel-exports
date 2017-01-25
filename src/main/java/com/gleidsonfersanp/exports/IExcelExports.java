package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.extra.exception.GeneralException;

public interface IExcelExports {

	File exportsForLocalPath(ExportQuery exportQuery, String path, String fileName) throws IOException, SQLException, GeneralException;

}
