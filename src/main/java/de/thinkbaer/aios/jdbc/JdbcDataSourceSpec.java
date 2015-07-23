package de.thinkbaer.aios.jdbc;



import de.thinkbaer.aios.api.datasource.DataSourceSpec;

public class JdbcDataSourceSpec extends DataSourceSpec{
	
	
	private String url;
	
	private String driver;
	
	private String driverLocation;
	
	private String user;
	
	private String password;
	
	private int maxPartials = 2;
	
	private int maxConnectionPerPartial = 20;

	
	
	public JdbcDataSourceSpec() {
		setType("jdbc");
	}
	/*
	public boolean validate(){
		Objects.requireNonNull(getType());
		Objects.requireNonNull(getName());
		Objects.requireNonNull(getUri());
		Objects.requireNonNull(getDriver());
		Objects.requireNonNull(getDriverLocation());
		Objects.requireNonNull(getUser());
		Objects.requireNonNull(getPassword());
	}
*/


	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverLocation() {
		return driverLocation;
	}

	public void setDriverLocation(String driverLocation) {
		this.driverLocation = driverLocation;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getMaxPartials() {
		return maxPartials;
	}
	public void setMaxPartials(int maxPartials) {
		this.maxPartials = maxPartials;
	}
	public int getMaxConnectionPerPartial() {
		return maxConnectionPerPartial;
	}
	public void setMaxConnectionPerPartial(int maxConnectionPerPartial) {
		this.maxConnectionPerPartial = maxConnectionPerPartial;
	}
	
	
	public String toString(){
		String str = "JDBCConnection data:\nname: "+getName()+"\nurl: "+getUrl()+"\nuser: "+getUser()+"\npass: "+getPassword()+"";
		return str;
	}
	
}
