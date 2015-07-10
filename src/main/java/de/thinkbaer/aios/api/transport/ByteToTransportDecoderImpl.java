package de.thinkbaer.aios.api.transport;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thinkbaer.aios.api.Utils;
import de.thinkbaer.aios.api.exception.Todo;
import de.undercouch.bson4jackson.BsonFactory;

public class ByteToTransportDecoderImpl extends ByteToMessageDecoder implements ByteToTransportDecoder {
	
		
	private static final Logger L = LogManager.getLogger( ByteToTransportDecoderImpl.class );
	
	@Inject
	private TransportDispatcher dispatcher;
	
		
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(!(in instanceof EmptyByteBuf)){
			
			int totalLength = in.readInt();			
			int headerLength = in.readInt();
			
			ByteBuf head = in.readBytes(headerLength);

			ByteBufInputStream bais = new ByteBufInputStream(head);
			ObjectMapper mapper = Utils.defaultBsonMapper();
			TransportSpec envelope = mapper.readValue(bais,TransportSpec.class);						
			head.release();
			
			Class<? extends Transport> payloadClass = dispatcher.classFor(envelope);
			
			int payloadLength = totalLength - headerLength;
			
			if(payloadLength > 0){
				ByteBuf payload = in.readBytes((totalLength - headerLength));
				bais = new ByteBufInputStream(payload);
				
				Transport ex = mapper.readValue(bais, payloadClass);	
				ex.setSpec(envelope);
				payload.release();

				L.debug("Length of head="+headerLength+" payload="+(totalLength-headerLength)+" total="+totalLength);
				if(totalLength > 0){
					L.debug("B2M: \n" + envelope);
					out.add(ex);		
				}else{
					new Todo();
				}
			}else{
				throw new Todo();
			}
			
			
		}
		
		
		
		
		
				
	}
	

}
