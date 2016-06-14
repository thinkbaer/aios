package de.thinkbaer.aios.api.datasource.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.thinkbaer.aios.jdbc.query.ExecuteBatchQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteBatchResultsImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteResultsImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteUpdateResultsImpl;
import de.thinkbaer.aios.jdbc.query.SelectResultsImpl;
import de.thinkbaer.aios.jdbc.struct.SchemaQueryImpl;
import de.thinkbaer.aios.jdbc.struct.SchemaResultsImpl;
import de.thinkbaer.aios.jdbc.struct.TableQueryImpl;
import de.thinkbaer.aios.jdbc.struct.TableResultsImpl;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "@t")  
	
@JsonSubTypes({  
    @Type(value = SelectResultsImpl.class, name = "jdbc.r.select"),
    @Type(value = SchemaResultsImpl.class, name = "jdbc.r.schema"),
    @Type(value = TableResultsImpl.class, name = "jdbc.r.table"),
    @Type(value = ExecuteResultsImpl.class, name = "jdbc.r.exec"),
    @Type(value = ExecuteUpdateResultsImpl.class, name = "jdbc.r.update"),
    @Type(value = ExecuteBatchResultsImpl.class, name = "jdbc.r.batch"),    
})  
public interface QueryResults {

}
