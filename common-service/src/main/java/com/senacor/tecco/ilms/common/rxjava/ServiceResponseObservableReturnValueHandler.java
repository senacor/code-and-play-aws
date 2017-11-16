package com.senacor.tecco.ilms.common.rxjava;

import com.senacor.tecco.ilms.common.response.ServiceResponse;
import io.jmnarloch.spring.boot.rxjava.async.SingleDeferredResult;
import io.jmnarloch.spring.boot.rxjava.mvc.ObservableReturnValueHandler;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.ModelAndViewContainer;
import rx.Observable;
import rx.Single;

/**
 * @author Andreas Keefer
 */
public class ServiceResponseObservableReturnValueHandler extends ObservableReturnValueHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }

        //final Observable<?> observable = Observable.class.cast(returnValue);
        //WebAsyncUtils.getAsyncManager(webRequest)
        //        .startDeferredResultProcessing(new ServiceResponseObservableDeferredResult(observable), mavContainer);

        final Single<ServiceResponse<?>> single = Observable.class.cast(returnValue)
                .toList()
                .map(ServiceResponse::new)
                .toSingle();
        WebAsyncUtils.getAsyncManager(webRequest)
                .startDeferredResultProcessing(new SingleDeferredResult(single), mavContainer);

    }
}
