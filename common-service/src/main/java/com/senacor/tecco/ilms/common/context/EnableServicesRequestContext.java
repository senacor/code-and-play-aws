package com.senacor.tecco.ilms.common.context;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface EnableServicesRequestContext.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ServicesRequestContextRegistration.class)
public @interface EnableServicesRequestContext {
}
