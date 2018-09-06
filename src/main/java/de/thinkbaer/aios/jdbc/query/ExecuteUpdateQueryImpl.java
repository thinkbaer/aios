package de.thinkbaer.aios.jdbc.query;


import java.sql.ResultSet;
import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.ModifyQuery;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class ExecuteUpdateQueryImpl extends AbstractSqlQueryImpl<ExecuteUpdateResultsImpl,ExecuteUpdateQueryImpl> implements ModifyQuery<ExecuteUpdateResultsImpl>{
/*
	private String query;
	
	public String queryAsString() {
		return query;
	}
*/
	@Override
	public ExecuteUpdateResultsImpl newResultsObject() {
		return new ExecuteUpdateResultsImpl();
	}

	@Override
	public ExecuteUpdateResultsImpl execute(ConnectionImpl conn) throws Exception {
		ExecuteUpdateResultsImpl results = new ExecuteUpdateResultsImpl();
		Statement stmt = null;
		try {
			stmt = tryAcquireStatement(conn);
			int affected = stmt.executeUpdate(getSql(),Statement.RETURN_GENERATED_KEYS);
			results.affected(affected);
			if(affected > 0) {
				ResultSet genKeySet = stmt.getGeneratedKeys();
				while(genKeySet.next()) {
					DataSetImpl dset = new DataSetImpl(genKeySet);
					results.push(dset);
				}				
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return results;
	}
}
