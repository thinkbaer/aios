package de.thinkbaer.aios.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.Utils;
import de.thinkbaer.aios.tests.jdbc.DB;

public class CreateTestEnviroment {

	private static final Logger L = LogManager.getLogger(CreateTestEnviroment.class);

	private String testsEnvDir = System.getProperty("user.dir") + "/env/tests";
	private String libsDir = testsEnvDir + "/libs";
	private String envDir;
	private String driverFile;

	public CreateTestEnviroment(DB db) throws IOException {
		TestUtils.createDir(testsEnvDir);
		TestUtils.createDir(libsDir);
		envDir = testsEnvDir + "/" + db.getName();
		TestUtils.clearAndCreateDir(envDir);

		String _url = db.getDriverUrl();
		URL url = new URL(_url);

		Pattern p = Pattern.compile("\\/([^\\/]+\\.jar)$");
		Matcher m = p.matcher(_url);
		if (m.find()) {
			String filename = m.group(1);
			L.debug(filename);
			String driverPath = libsDir + "/" + filename;
			db.setDriverPath(driverPath);
			File file = new File(driverPath);
			if (!file.exists()) {
				try {
					Utils.copy(url, file);
				} catch (IOException e) {
					L.throwing(e);
				}
			}

			if (db.testDriverConfiguration()) {
				try {
					Connection c = DriverManager.getConnection("jdbc:hsqldb:file:" + envDir + "/dbfile", "SA", "");
					update(c, "CREATE TABLE  IF NOT EXISTS car ( id INTEGER IDENTITY, type VARCHAR(256), name VARCHAR(256))");
					update(c, "CREATE TABLE  IF NOT EXISTS owner ( id INTEGER IDENTITY, surname VARCHAR(256), givenName VARCHAR(256))");
					update(c,
			                "INSERT INTO car (type, name) VALUES('Ford', 'Mustang')");
					update(c,
			                "INSERT INTO car (type, name) VALUES('Ford', 'Fiesta')");
					update(c,
			                "INSERT INTO owner (surname,givenName) VALUES('Ford', 'Henry')");
					
					shutdown(c);
					L.debug("Test DB created");
				} catch (SQLException e) {
					L.error(e);
				}
			}

		}

	}
	
	public void shutdown(Connection conn) throws SQLException {

        Statement st = conn.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        st.execute("SHUTDOWN");
        conn.close();    // if there are no other open connection
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

}
