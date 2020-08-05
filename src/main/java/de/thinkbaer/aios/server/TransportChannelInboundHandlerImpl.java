package de.thinkbaer.aios.server;

import de.thinkbaer.aios.api.exception.AiosException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.transport.Transport;
import de.thinkbaer.aios.server.action.OperationResponseHandler;

public class TransportChannelInboundHandlerImpl extends SimpleChannelInboundHandler<Transport> implements
  TransportChannelInboundHandler {

  // private static final Logger L = LogManager.getLogger(
  // ExchangeChannelInboundHandlerImpl.class );

  private Logger L;


  @Inject
  private ResponseHandlerDispatcher dispatcher;


  public TransportChannelInboundHandlerImpl() {
    super(true);
    L = LogManager.getLogger("channel.inbound.server");

  }


  protected void channelRead0(ChannelHandlerContext ctx, Transport exchange_2) throws Exception {
    if (exchange_2 instanceof OperationRequest) {
      OperationResponseHandler<?, ?, ?> oph = dispatcher.inject(ctx.channel(), (OperationRequest) exchange_2);

      ctx.channel().eventLoop().execute(new Runnable() {
        @Override
        public void run() {
          try {
            oph.execute();
          } catch (Exception e) {
            L.error("processing error: " + e.getMessage());
            L.throwing(e);
            Transport response = oph.createResponse();
            response.addError(new AiosException(e).asErrorMessage());
            ctx.channel().writeAndFlush(response);
          }
        }
      });
    }

  }

  private void handleResponse(ChannelHandlerContext ctx, Transport response, boolean sync) throws InterruptedException {
    try {
      ChannelFuture writeFuture = ctx.write(response);
      if (sync) {
        writeFuture.sync();
      }
    } catch (Exception e) {
      L.throwing(e);
    }
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    L.throwing(cause);
    L.debug("Channel closed");
    ctx.close();
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) {
    // Once session is secured, send a greeting and register the channel to
    // the global channel
    // list so the channel received the messages from others.
    L.debug("Channel active");

  }

}
