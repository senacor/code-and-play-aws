package com.senacor.tecco.ilms.common.context.demo;

import com.senacor.tecco.ilms.common.context.ServicesRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Single;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Andreas Keefer
 */
@RestController
@RequestMapping(value = "/services/demo")
public class ContextDemoControler {

    private static final Logger logger = LoggerFactory.getLogger(ContextDemoControler.class);

    @Autowired
    private DemoService demoService;

    @RequestMapping(method = RequestMethod.GET, value = "/hello/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Observable<Demo> hello(@PathVariable("name") final String name) {
        print("/demo/hello/" + name);
        return demoService.hello(name);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/helloSingle/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Single<Demo> helloSingle(@PathVariable("name") final String name) {
        print("/demo/hello/" + name);
        return demoService.hello(name).toSingle();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/helloBlocking/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Demo helloBlocking(@PathVariable("name") final String name) {
        print("/demo/helloBlocking/" + name);
        return demoService.hello(name).toBlocking().first();
    }

    @RequestMapping(value = "/helloHystrix/{errorRate}", method = GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Demo helloHystrix(@PathVariable("errorRate") int errorRate) {
        print("DemoControler.helloHystrix(%s)", errorRate);
        return demoService.helloHystrix(errorRate);
    }

    @RequestMapping(value = "/helloHystrixObservable/{errorRate}", method = GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Observable<Demo> helloHystrixObservable(@PathVariable("errorRate") int errorRate) {
        print("DemoControler.helloHystrixObservable(%s)", errorRate);
        return demoService.helloHystrixObservable(errorRate);
    }

    @RequestMapping(value = "/helloHystrixSingle/{errorRate}", method = GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Single<Demo> helloHystrixSingle(@PathVariable("errorRate") int errorRate) {
        print("DemoControler.helloHystrixSingle(%s)", errorRate);
        return demoService.helloHystrixObservable(errorRate)
                .toSingle();
    }

    private void print(String msg, Object... args) {
        logger.info(Thread.currentThread().getName() + "(" + ServicesRequestContextHolder.get().getRequestID() + ") " +
                String.format(msg, args));
    }
}
