package de.thinkbaer.aios.api.utils;

public interface GenericObjectFactory {

	public <X> X inject(Class<X> cls);

}
