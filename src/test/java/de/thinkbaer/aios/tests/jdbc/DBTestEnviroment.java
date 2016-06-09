package de.thinkbaer.aios.tests.jdbc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.Utils;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;

import de.thinkbaer.aios.tests.TestUtils;

public class DBTestEnviroment {
	private static final Logger L = LogManager.getLogger(DBTestEnviroment.class);
	private static Map<String, Class<? extends DB>> types = new HashMap<>();

	private String testsEnvDir = System.getProperty("user.dir") + "/env/tests";
	private String libsDir = testsEnvDir + "/libs";
	//private String envDir;

	static {
		types.put("hsqldb", HsqlDB.class);
		types.put("sqlite", SqliteDB.class);
		//types.put("mysql", MysqlDB.class);
	}

	private DB db;

	private String dbType;

	public DBTestEnviroment(String string, String envDir, JdbcDataSourceSpec spec) throws InstantiationException, IllegalAccessException, IOException {
		this.dbType = string;


		Class<? extends DB> dbClass = types.get(this.dbType);
		this.db = dbClass.newInstance();
		
		
		if(spec != null){
			this.db.spec(spec);
		}
		
		TestUtils.createDir(testsEnvDir);
		TestUtils.createDir(libsDir);		
		TestUtils.clearAndCreateDir(this.db.getEnvDir());


		String _url = db.getDriverUrl();
		URL url = new URL(_url);

		String fileName = Utils.getJarFileName(_url);
		Objects.requireNonNull(fileName);
		L.debug(fileName);
		
		String driverPath = libsDir + "/" + fileName;
		db.setDriverPath(driverPath);
		
		File file = new File(driverPath);
		if (!file.exists()) {
			try {
				Utils.copy(url, file);
			} catch (IOException e) {
				L.throwing(e);
			}
		}

		if(!db.testDriverConfiguration()){
			throw new NullPointerException("DB test failed");
		}
				
		
		
	}
	
	
	public DBTestEnviroment(String string) throws InstantiationException, IllegalAccessException, IOException {
		this(string,null, null);
	}

	public void cleanUp() {

		try {
			db.shutdown();
		} catch (Exception e) {
		}
		try {
			TestUtils.clearDirectory(db.getTestEnvDir());
		} catch (IOException e) {
		}
	}

	public DB getDB() {
		return db;
	}

}
