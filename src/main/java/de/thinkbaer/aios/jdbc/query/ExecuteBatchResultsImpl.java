package de.thinkbaer.aios.jdbc.query;

import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.ModifyQuery;
import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class ExecuteBatchResultsImpl implements QueryResults {

	private int[] batchResults;
	
	
	public void batchDone(int[] ret) {
		batchResults = ret;		
	}


	public int[] getBatchResults() {
		return batchResults;
	}


	public void setBatchResults(int[] batchResults) {
		this.batchResults = batchResults;
	}


}
