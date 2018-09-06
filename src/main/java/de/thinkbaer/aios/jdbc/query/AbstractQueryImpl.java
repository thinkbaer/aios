package de.thinkbaer.aios.jdbc.query;


import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.jdbc.ConnectionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractQueryImpl<X extends QueryResults, Y extends AbstractQueryImpl> {

	// private String sql;
	
	private static final Logger L = LogManager.getLogger(AbstractQueryImpl.class);
	
	public abstract X execute(ConnectionImpl conn)  throws Exception;
	
	public Statement tryAcquireStatement(ConnectionImpl connection) throws Exception{
		return tryAcquireStatement(connection, 5, 500);		
	}
	
	
	public Statement tryAcquireStatement(ConnectionImpl connection, int retry, int sleep) throws Exception{
		Statement stat = null;
		try{
			stat = connection.getConnection().createStatement();			
		}catch(Exception e){
			if(retry > 0){
			    L.warn("tryAcquireStatement problem: " + e.getMessage() + " Retry: " + retry);

				connection.close();
				Thread.sleep(sleep);
				return tryAcquireStatement(connection, --retry, sleep);
			}else{
				throw e;
			}
		}
		return stat;	
	}


}
