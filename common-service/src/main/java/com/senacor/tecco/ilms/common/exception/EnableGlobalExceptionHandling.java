package com.senacor.tecco.ilms.common.exception;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by mmenzel on 23.06.2016.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalExceptionHandler.class, GlobalErrorController.class})
public @interface EnableGlobalExceptionHandling {
}
