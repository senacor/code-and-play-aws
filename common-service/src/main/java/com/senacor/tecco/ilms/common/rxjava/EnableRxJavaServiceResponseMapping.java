package com.senacor.tecco.ilms.common.rxjava;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Mapping of ReturnType Observable<T> / Single<T> to ServiceResponse<List<T> / <T>>
 * That means you can return Observable<T> / Single<T> in your Controller and it will be mapped to
 * ServiceResponse<List<T>> / ServiceResponse<T>
 *
 * @author Andreas Keefer
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RxJavaServiceRequestConfig.class)
public @interface EnableRxJavaServiceResponseMapping {
}
