package de.thinkbaer.aios.tests.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HsqlDB extends DB {

	
	
	
	public HsqlDB() {
		setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		setName("hsql_testdb");
		setEnvDir(getTestEnvDir() + "/" + getName());
		setConnectionUrl("jdbc:hsqldb:file:" + getEnvDir() + "/db");
		setDriverUrl("http://central.maven.org/maven2/org/hsqldb/hsqldb/2.3.3/hsqldb-2.3.3.jar");
		setUserName("SA");
		setPassword("");
	}
	
	
	@Override
	public void createDB(String classifier) throws Exception {
		Connection c = getConnection();
		update(c, "DROP TABLE  driver IF EXISTS ");
		update(c, "DROP TABLE  car IF EXISTS ");
		update(c, "DROP TABLE owner  IF EXISTS ");
		update(c, "CREATE TABLE  IF NOT EXISTS car ( id INTEGER IDENTITY, type VARCHAR(256), name VARCHAR(256))");
		update(c, "CREATE TABLE  IF NOT EXISTS owner ( id INTEGER IDENTITY, surname VARCHAR(256), givenName VARCHAR(256))");
		update(c, "CREATE TABLE  IF NOT EXISTS driver ( id INTEGER IDENTITY, car_id INTEGER, owner_id INTEGER)");
		update(c, "ALTER TABLE PUBLIC.driver ADD FOREIGN KEY (car_id) REFERENCES PUBLIC.car(id)");
		update(c, "ALTER TABLE PUBLIC.driver ADD FOREIGN KEY (owner_id) REFERENCES PUBLIC.owner(id)");
		update(c, "INSERT INTO car (type, name) VALUES('Ford', 'Mustang')");
		update(c, "INSERT INTO car (type, name) VALUES('Ford', 'Fiesta')");
		update(c, "INSERT INTO owner (surname,givenName) VALUES('Ford', 'Henry')");
		
		update(c, "INSERT INTO driver (car_id,owner_id) VALUES((select max(id) from car),(select max(id) from owner))");
	}
	

	@Override
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(getConnectionUrl(), getUserName(), getPassword());
	}
	
	@Override
	public void shutdown() throws SQLException {
		Connection c = getConnection();
		Statement st = c.createStatement();
		st.execute("SHUTDOWN");
		c.close();
	}

}
