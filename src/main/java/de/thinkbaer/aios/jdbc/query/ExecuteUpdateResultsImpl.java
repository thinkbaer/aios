package de.thinkbaer.aios.jdbc.query;

import de.thinkbaer.aios.api.datasource.query.QueryResults;

public class ExecuteUpdateResultsImpl implements QueryResults{

	private int affected;
	

	public void affected(int affected) {
		this.affected = affected;
	}


	public int getAffected() {
		return affected;
	}


	public void setAffected(int affected) {
		this.affected = affected;
	}

}
