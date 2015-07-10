package de.thinkbaer.aios.jdbc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.Utils;

public class DriverManagerImpl implements DriverManager {

	private static final Logger L = LogManager.getLogger(DriverManagerImpl.class);

	private List<String> driverClasses = new ArrayList<>();

	private String envDir = System.getProperty("user.dir") + "/env/libs";

	public DriverManagerImpl() throws IOException {
		Utils.createDir(envDir);
	}

	public void registerIfNotExists(String driver, String driverLocation) throws Exception {
		if (!driverClasses.contains(driver)) {
			L.warn("Driver " + driver + " doesn't exists.");

			File to = null;
			// check if driverLocation is local! then we don't need to
			// download driver

			if (!Utils.isFilenameValid(driverLocation)) {

				String fileName = Utils.getJarFileName(driverLocation);
				if (fileName == null) {
					throw new NullPointerException("No jar found in driver location " + driverLocation);
				}
				String fileJar = envDir + "/" + fileName;


				to = new File(fileJar);
				if (!to.exists()) {
					URL from = new URL(driverLocation);
					L.debug("Downloading driver file " + from.toString() + " to " + to.getAbsolutePath());
					Utils.copy(from, to);
				} else {
					L.debug("Driver file " + to.getAbsolutePath() + " exists");
				}
			}else{
				to = new File(driverLocation);
				if(!to.exists()){
					throw new NullPointerException("Local driver location doesn't exist: " + driverLocation);
				}
			}
			load(driver, to);
		} else {
			L.debug("Driver " + driver + " already loaded.");
		}
	}

	private void load(String driverClassName, File to) throws Exception {
		try {
			Class<?> loadedClass = Class.forName(driverClassName, true, this.getClass().getClassLoader());
			Driver driver = (Driver) loadedClass.newInstance();
			L.debug("Driver: " + driver + " loaded!!! " + driver.getClass());
			driverClasses.add(driver.getClass().getName());
		} catch (Exception ex) {
			L.debug("Driver: " + driverClassName + " not found. Try load from " + to.getAbsolutePath());
			URL[] urls;
			try {
				urls = new URL[] { new URL("jar", "", "file:" + to.getAbsolutePath() + "!/") };

				URLClassLoader cl = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());
				Class<?> loadedClass = cl.loadClass(driverClassName);
				Driver driver = (Driver) loadedClass.newInstance();
				java.sql.DriverManager.registerDriver(new DriverShim(driver));
				L.debug("Driver: " + driverClassName + " loaded!!! " + driver.getClass());
				driverClasses.add(driver.getClass().getName());
			} catch (Exception e) {
				throw e;
			}
		}

	}

}
