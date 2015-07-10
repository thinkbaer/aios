package de.thinkbaer.aios.jdbc.query;

import java.sql.ResultSet;
import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.ModifyQuery;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class ExecuteQueryImpl extends AbstractQueryImpl<ExecuteResultsImpl,ExecuteQueryImpl> implements ModifyQuery<ExecuteResultsImpl>{

	@Override
	public ExecuteResultsImpl newResultsObject() {
		return new ExecuteResultsImpl();
	}

	@Override
	public ExecuteResultsImpl execute(ConnectionImpl conn) throws Exception {
		ExecuteResultsImpl results = new ExecuteResultsImpl();
		Statement stmt = null;
		try {
			stmt = tryAcquireStatement(conn);
			boolean okay = stmt.execute(getSql());
			results.status(okay);
			if(okay){
				ResultSet rs = stmt.getResultSet();				
				while (rs.next()) {
					DataSetImpl dset = new DataSetImpl(rs);
					results.push(dset);
				}
				rs.close();
			}else{
				results.updateCount(stmt.getUpdateCount());	
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
