package de.thinkbaer.aios.jdbc.query;


import java.sql.SQLException;
import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.datasource.query.SearchQuery;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public abstract class AbstractQueryImpl<X extends QueryResults, Y extends AbstractQueryImpl> {

	private String sql;
	
	
	public abstract X execute(ConnectionImpl conn)  throws Exception;
	
	public Statement tryAcquireStatement(ConnectionImpl connection) throws Exception{
		return tryAcquireStatement(connection, 5, 100);
		
	}
	
	public Statement tryAcquireStatement(ConnectionImpl connection, int retry, int sleep) throws Exception{
		Statement stat = null;
		try{
			stat = connection.getConnection().createStatement();
		}catch(SQLException | AiosException e){
			if(retry > 0){
				connection.close();
				Thread.sleep(sleep);
				return tryAcquireStatement(connection, --retry, sleep);
			}else{
				throw e;
			}
		}
		return stat;
		
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public Y sql(String query){
		setSql(query);
		return (Y) this;
	}
	
	/*
	private Statement statement;
	
	public X execute(Connection conn){
		statement = conn.createStatement();

		int i = statement.executeUpdate(sql);
		if (i == -1) {
			L.error("db error : " + sql + " " + st.getWarnings());
		}
		statement.close();
	}
	
	public Statement statement(){
		return statement;
	}
	
	public X results() {
		return null;
	}
	*/

}
