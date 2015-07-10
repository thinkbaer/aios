package de.thinkbaer.aios.api.utils;

import javax.inject.Inject;

import com.google.inject.Injector;

public class GenericObjectFactoryImpl implements GenericObjectFactory{

	@Inject
	private Injector injector;
	
	@Override
	public <X> X inject(Class<X> cls){
		return injector.getInstance(cls);
	}

	public Injector getInjector() {
		return injector;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}
	
	
}
