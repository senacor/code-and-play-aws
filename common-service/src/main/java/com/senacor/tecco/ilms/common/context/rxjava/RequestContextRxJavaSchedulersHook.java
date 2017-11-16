package com.senacor.tecco.ilms.common.context.rxjava;

import com.senacor.tecco.ilms.common.context.ServicesRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action0;
import rx.plugins.RxJavaSchedulersHook;

/**
 * has to be set via spring config:
 * rxjava.plugin.RxJavaSchedulersHook.implementation=com.senacor.tecco.ilms.common.context.rxjava.RequestContextSchedulersHook
 * or via
 * RxJavaPlugins.getInstance().registerSchedulersHook(new RequestContextSchedulersHook());
 * But this Method can only called once in a VM otherwise you get an IllegalStateException
 *
 * @author Andreas Keefer
 */
public class RequestContextRxJavaSchedulersHook extends RxJavaSchedulersHook {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextRxJavaSchedulersHook.class);

    @Override
    public Action0 onSchedule(Action0 action) {
        return super.onSchedule(new WrappedAction0(action));
    }

    private static final class WrappedAction0 implements Action0 {

        private final Action0 actual;

        private final ServicesRequestContextHolder context;

        public WrappedAction0(Action0 actual) {
            this.actual = actual;
            this.context = ServicesRequestContextHolder.get();
        }

        @Override
        public void call() {
            print("RequestContextSchedulersHook.setContext...");
            ServicesRequestContextHolder.set(context);
            print("RequestContextSchedulersHook.setContext...done");
            actual.call();
            print("RequestContextSchedulersHook.clear...");
            ServicesRequestContextHolder.clear();
            print("RequestContextSchedulersHook.clear...done");
        }

        private void print(String msg, Object... args) {
            if (logger.isDebugEnabled()) {
                logger.debug(Thread.currentThread().getName() + "(" + ServicesRequestContextHolder.get().getRequestID() + ") " +
                        String.format(msg, args));
            }
        }
    }
}
