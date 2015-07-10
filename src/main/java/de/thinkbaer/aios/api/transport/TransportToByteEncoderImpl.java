package de.thinkbaer.aios.api.transport;

import javax.inject.Inject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.thinkbaer.aios.api.Utils;

public class TransportToByteEncoderImpl extends io.netty.handler.codec.MessageToByteEncoder<Transport> implements TranportToByteEncoder {

	private static final Logger L = LogManager.getLogger(TransportToByteEncoderImpl.class);

	@Inject
	private TransportDispatcher dispatcher;

	@Override
	protected void encode(ChannelHandlerContext ctx, Transport msg, ByteBuf out) throws Exception {

		ByteBuf head = ctx.alloc().buffer();
		ByteBuf payload = ctx.alloc().buffer();

		ByteBufOutputStream baos = new ByteBufOutputStream(head);
		ObjectMapper mapper = Utils.defaultBsonMapper();

		TransportSpec desc = dispatcher.specOf(msg);
		mapper.writeValue(baos, desc);

		baos = new ByteBufOutputStream(payload);
		mapper.writeValue(baos, msg);

		int headerLength = head.readableBytes();
		int payloadLength = payload.readableBytes();
		int totalLength = headerLength + payloadLength;

		L.debug("M2B: \n" + desc);
		L.debug("Length of head=" + headerLength + " payload=" + payloadLength + " total=" + totalLength);

		out.writeInt(totalLength);
		out.writeInt(headerLength);
		out.writeBytes(head);
		if (payloadLength > 0) {
			out.writeBytes(payload);
		}
		head.release();

		payload.release();

	}

}
