package com.senacor.tecco.ilms.common.context.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.senacor.tecco.ilms.common.context.ServicesRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hook which will set the context from the ContextHystrixRequest back to the current Thread
 * <p>
 * User: Andreas Keefer, Senacor Technologies AG
 * Date: 12.05.2016
 * Time: 17:44
 *
 * @author Andreas Keefer (andreas.keefer@senacor.com), Senacor Technologies AG
 */
public class ContextHystrixRegistratorCommandHook extends HystrixCommandExecutionHook {

    private static final Logger logger = LoggerFactory.getLogger(ContextHystrixRegistratorCommandHook.class);

    @Override
    public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
        ServicesRequestContextHolder ctx = ContextHystrixRequestVariable.getInstance().get();
        print("ContextRegistratorCommandHook.onExecutionStart: " + ctx);
        ServicesRequestContextHolder.set(ctx);
        super.onStart(commandInstance);
    }

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType,
                                 Exception e) {
        print("ContextRegistratorCommandHook.onError: clearContext...");
        ServicesRequestContextHolder.clear();
        print("ContextRegistratorCommandHook.onError: clearContext... done");
        return super.onError(commandInstance, failureType, e);
    }

    @Override
    public <T> void onSuccess(HystrixInvokable<T> commandInstance) {
        super.onSuccess(commandInstance);
        print("ContextRegistratorCommandHook.onSuccess: clearContext...");
        ServicesRequestContextHolder.clear();
        print("ContextRegistratorCommandHook.onSuccess: clearContext... done");
    }

    private void print(String msg, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + "(" + ServicesRequestContextHolder.get().getRequestID() + ") " +
                    String.format(msg, args));
        }
    }
}
