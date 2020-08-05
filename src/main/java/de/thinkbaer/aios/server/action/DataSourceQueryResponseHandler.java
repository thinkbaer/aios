package de.thinkbaer.aios.server.action;


import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.DataSourceQueryRequest;
import de.thinkbaer.aios.api.action.DataSourceQueryResponse;
import de.thinkbaer.aios.api.action.DataSourceRequest;
import de.thinkbaer.aios.api.action.DataSourceResponse;
import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.transport.ErrorMessage;
import de.thinkbaer.aios.server.annotation.RequestHandler;
import de.thinkbaer.aios.server.datasource.DataSourceManager;

@RequestHandler(request = DataSourceQueryRequest.class, response = DataSourceQueryResponse.class)
public class DataSourceQueryResponseHandler extends OperationResponseHandler<DataSourceQueryRequest, DataSourceQueryResponse, DataSourceQueryResponseHandler> {

  private static final Logger L = LogManager.getLogger(DataSourceQueryResponseHandler.class);

  @Inject
  private DataSourceManager manager;

  @Override
  public DataSourceQueryResponse createResponse() {
    DataSourceQueryResponse response = new DataSourceQueryResponse();
    response.getSpec().setRid(getRequestId());
    response.setDsn(request().getDsn());
    return response;
  }

  public void execute() {
    DataSourceQueryResponse response = this.createResponse();


    try {
      // get datasource
      DataSource dataSource = manager.get(request().getDsn());
      Connection connection;
      connection = dataSource.connection();
      QueryResults res = connection.query(request().getQuery());
      response.setResult(res);
      connection.close();
    } catch (Exception | Error e) {
      L.throwing(e);

      if (e instanceof AiosException) {
        response.addError(((AiosException) e).asErrorMessage());
      } else {
        response.addError(new AiosException(e).asErrorMessage());
      }
    }

    getChannel().writeAndFlush(response);
  }


}
