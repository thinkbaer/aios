package de.thinkbaer.aios.api.transport;



public interface TransportCodecFactory {

	public ByteToTransportDecoder newExchangeDecoder();

	public TranportToByteEncoder newExchangeEncoder();
	
	
	
	// public ClientExchangeChannelInboundHandler newClientExchangeChannelInboundHandler(String context);

}
