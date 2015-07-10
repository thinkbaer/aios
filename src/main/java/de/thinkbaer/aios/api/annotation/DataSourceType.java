package de.thinkbaer.aios.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.thinkbaer.aios.api.datasource.DataSourceSpec;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataSourceType {
	String name();

	Class<? extends DataSourceSpec> spec();
}
