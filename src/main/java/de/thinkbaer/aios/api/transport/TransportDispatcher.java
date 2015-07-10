package de.thinkbaer.aios.api.transport;

import de.thinkbaer.aios.api.transport.TransportDispatcherImpl.TransportDescriptor;

public interface TransportDispatcher {

	// public ExchangeHandler handlerFor(Exchange msg);
	public de.thinkbaer.aios.api.transport.TransportSpec specOf(Transport msg);
	public Class<? extends Transport> classFor(TransportSpec envelope);
	/*
	public <X> ExchangeHandler<X> handlerFor(Exchange exchange, Class<X> cls);

	public ExchangeDesc descriptorOf(Exchange msg);
*/
}
