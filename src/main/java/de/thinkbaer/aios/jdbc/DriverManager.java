package de.thinkbaer.aios.jdbc;

public interface DriverManager {

	public void registerIfNotExists(String driver, String driverLocation) throws Exception;

}
