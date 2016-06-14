package de.thinkbaer.aios.jdbc.query;




import de.thinkbaer.aios.api.datasource.query.QueryResults;

public abstract class AbstractSqlQueryImpl<X extends QueryResults, Y extends AbstractQueryImpl> extends AbstractQueryImpl<X,Y> {

	private String sql;
	
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

}
