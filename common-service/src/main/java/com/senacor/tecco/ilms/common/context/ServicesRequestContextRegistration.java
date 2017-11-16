package com.senacor.tecco.ilms.common.context;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.senacor.tecco.ilms.common.context.hystrix.ContextHystrixRegistratorCommandHook;
import com.senacor.tecco.ilms.common.filter.FilterOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * The Class ServicesRequestContextRegistration.
 */
public class ServicesRequestContextRegistration {

    private static final Logger logger = LoggerFactory.getLogger(ServicesRequestContextRegistration.class);

    @Value("${rxjava.plugin.RxJavaSchedulersHook.implementation:com.senacor.tecco.ilms.common.context.rxjava.RequestContextRxJavaSchedulersHook}")
    private String requestContextRxJavaSchedulersHook;

    /**
     * map spring property to SystemProperty
     */
    @Bean
    public String registerRequestContextRxJavaSchedulersHook() {
        final String key = "rxjava.plugin.RxJavaSchedulersHook.implementation";
        String value = requestContextRxJavaSchedulersHook;

        logger.info("set SystemProperty: " + key + "=" + value);
        System.setProperty(key, requestContextRxJavaSchedulersHook);

        return value;
    }

    /**
     * Gets the services request context registration.
     *
     * @return the services request context registration
     */
    @Bean
    public FilterRegistrationBean getServicesRequestContextRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ServicesRequestContextFilter());
        registration.addUrlPatterns("/services/*");
        registration.setOrder(FilterOrder.CONTEXT);
        return registration;
    }

    @Bean
    public ContextHystrixRegistratorCommandHook contextRegistratorCommandHook() {
        ContextHystrixRegistratorCommandHook hook = new ContextHystrixRegistratorCommandHook();
        try {
            HystrixPlugins.getInstance().registerCommandExecutionHook(hook);
        } catch (IllegalStateException e) {
            // seems that CommandExecutionHook is already registered
        }
        return hook;
    }
}
