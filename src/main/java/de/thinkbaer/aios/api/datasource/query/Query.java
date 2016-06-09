package de.thinkbaer.aios.api.datasource.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.thinkbaer.aios.jdbc.query.ExecuteBatchQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteUpdateQueryImpl;
import de.thinkbaer.aios.jdbc.query.SelectQueryImpl;
import de.thinkbaer.aios.jdbc.struct.SchemaQueryImpl;


@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "@t")  
	
@JsonSubTypes({  
    @Type(value = SelectQueryImpl.class, name = "jdbc.q.select"),
    @Type(value = SchemaQueryImpl.class, name = "jdbc.q.schema"),
    @Type(value = ExecuteQueryImpl.class, name = "jdbc.q.exec"),
    @Type(value = ExecuteUpdateQueryImpl.class, name = "jdbc.q.update"),
    @Type(value = ExecuteBatchQueryImpl.class, name = "jdbc.q.batch"),    
})  

public interface Query<X extends QueryResults> {

	public X newResultsObject();
}
