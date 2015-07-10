package de.thinkbaer.aios.tests.jdbc;

import java.sql.Connection;

public class SqliteDB extends DB {

	private String driverClass = "org.sqlite.JDBC";
	
	private String name = "sqlite";
	
	private String url = "http://central.maven.org/maven2/org/xerial/sqlite-jdbc/3.8.10.1/sqlite-jdbc-3.8.10.1.jar";
	
	@Override
	public String getDriverUrl() {
		return url;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDriverClassName() {
		return driverClass;
	}

	@Override
	public void createDB(String classifier) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getConnectionUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
