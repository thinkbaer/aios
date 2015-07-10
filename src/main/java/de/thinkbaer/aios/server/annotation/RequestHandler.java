package de.thinkbaer.aios.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RequestHandler {

	Class<? extends OperationRequest> request();
	Class<? extends OperationResponse> response();
}
