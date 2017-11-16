package com.senacor.tecco.ilms.common.rxjava;

import com.senacor.tecco.ilms.common.response.ServiceResponse;
import io.jmnarloch.spring.boot.rxjava.async.SingleDeferredResult;
import io.jmnarloch.spring.boot.rxjava.mvc.SingleReturnValueHandler;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.ModelAndViewContainer;
import rx.Single;

/**
 * @author Andreas Keefer
 */
public class ServiceResponseSingleReturnValueHandler extends SingleReturnValueHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }

        final Single<ServiceResponse<?>> single = Single.class.cast(returnValue)
                .map(ServiceResponse::new);
        WebAsyncUtils.getAsyncManager(webRequest)
                .startDeferredResultProcessing(new SingleDeferredResult(single), mavContainer);
    }
}
