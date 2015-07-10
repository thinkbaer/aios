package de.thinkbaer.aios.tests.jdbc;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.tests.CreateTestEnviroment;
import de.thinkbaer.aios.jdbc.DriverShim;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;

public abstract class DB {
	
	protected static String connectionClass = "java.sql.Connection";
	
	private static final Logger L = LogManager.getLogger( DB.class );
	
	public  static String testEnvDir = System.getProperty("user.dir") + "/env/tests";
	public  static String libsDir = testEnvDir + "/libs";
	private String envDir;

	
	
	private boolean test = false;
	
	private String driverPath;
	
	private String driverClassName;

	private String driverUrl;

	private String connectionUrl;
	
	private String userName;
	
	private String password;
	
	private String name;
	
	public void spec(JdbcDataSourceSpec spec){
		setName(spec.getName());
		setEnvDir(getTestEnvDir() + "/" + getName());
		setConnectionUrl(spec.getUrl());
		setDriverUrl(spec.getDriverLocation());
		setUserName(spec.getUser());
		setPassword(spec.getPassword());		
	}

	
	public boolean testDriverConfiguration() {
		if(test){
			return test;
		}
		
		String driverClassName = getDriverClassName();
		String driverPath = getDriverPath();
		L.debug("try loading [" + driverClassName + "] from " + driverPath);

		try {
			Class<?> loadedClass = Class.forName(driverClassName, true, this.getClass().getClassLoader());
			Driver driver = (Driver) loadedClass.newInstance();
			L.debug("Driver: " + driverClassName + " loaded!!! " + driver.getClass());
			test = true;			
		} catch (Exception ex) {
			L.debug("Driver: " + driverClassName + " not found. Try load from "
					+ driverPath);
			URL[] urls;
			try {
				urls = new URL[] { new URL("jar", "", "file:" + driverPath + "!/") };
				
				URLClassLoader cl = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());
				Class<?> loadedClass = cl.loadClass(driverClassName);
				Driver driver = (Driver)loadedClass.newInstance();
				DriverManager.registerDriver(new DriverShim(driver));
				L.debug("Driver: " + driverClassName + " loaded!!! " + driver.getClass());
				test = true;				
			} catch (MalformedURLException e) {
				L.throwing(e);
			} catch (ClassNotFoundException e) {
				L.throwing(e);
			} catch (InstantiationException e) {
				L.throwing(e);
			} catch (IllegalAccessException e) {
				L.throwing(e);
			} catch (SQLException e) {
				L.throwing(e);
			}
		}
		return test;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	
	public abstract void createDB(String classifier) throws Exception;
	
	public abstract void shutdown() throws Exception;

	public String getTestEnvDir() {
		return testEnvDir;
	}

	public void setTestEnvDir(String testEnvDir) {
		this.testEnvDir = testEnvDir;
	}
	
	public void update(Connection c, String sql) {

		Statement st = null;
		
		try {
			st = c.createStatement();
			
			int i = st.executeUpdate(sql);
			if (i == -1) {
				L.error("db error : " + sql + " " + st.getWarnings());
			}
			st.close();
		} catch (SQLException e) {
			L.throwing(e);
		} finally {
			
			
		}

	}

	public abstract Connection getConnection() throws Exception;

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getDriverUrl() {
		return driverUrl;
	}

	public void setDriverUrl(String driverUrl) {
		this.driverUrl = driverUrl;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnvDir() {
		return envDir;
	}

	public void setEnvDir(String envDir) {
		this.envDir = envDir;
	}

	

	public String getLibsDir() {
		return libsDir;
	}

	public void setLibsDir(String libsDir) {
		this.libsDir = libsDir;
	}
	
	
}
