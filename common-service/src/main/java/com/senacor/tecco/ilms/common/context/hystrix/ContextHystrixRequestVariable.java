package com.senacor.tecco.ilms.common.context.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.senacor.tecco.ilms.common.context.ServicesRequestContextHolder;

/**
 * The ContextHystrixRequestVariable is a holder which will allow us to access the Context. The HystrixRequestVariableDefault
 * implementation is able to get the right SecurityContext for concurrent requests as long as the same instance of
 * HystrixRequestVariableDefault is used.
 * <p>
 * User: Andreas Keefer, Senacor Technologies AG
 * Date: 12.05.2016
 * Time: 18:19
 *
 * @author Andreas Keefer (andreas.keefer@senacor.com), Senacor Technologies AG
 */
public class ContextHystrixRequestVariable {

    private static final HystrixRequestVariableDefault<ServicesRequestContextHolder> context = new HystrixRequestVariableDefault<>();

    private ContextHystrixRequestVariable() {
    }

    public static HystrixRequestVariableDefault<ServicesRequestContextHolder> getInstance() {
        return context;
    }
}
