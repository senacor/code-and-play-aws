package com.senacor.tecco.ilms.common.context.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.senacor.tecco.ilms.common.context.ServicesRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Random;

/**
 * Demo Service
 * <p>
 * User: Andreas Keefer, Senacor Technologies AG
 * Date: 12.05.2016
 * Time: 13:47
 *
 * @author Andreas Keefer (andreas.keefer@senacor.com), Senacor Technologies AG
 */
@Service
public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    private Random random = new Random();

    public Observable<Demo> hello(final String name) {
        return Observable.<Demo>create(subscriber -> {
            try {
                print("emitting new Demo...");
                Thread.sleep(1000);
                subscriber.onNext(new Demo("Hello ", name));
                print("emitting new Demo...done");
                subscriber.onCompleted();
            } catch (Throwable t) {
                subscriber.onError(t);
            }
        }).subscribeOn(Schedulers.io());
    }

    @HystrixCommand(fallbackMethod = "helloHystrixFallback")
    public Demo helloHystrix(int errorRate) {
        return helloInternal(errorRate);
    }

    private Demo helloInternal(int errorRate) {
        print("helloHystrix(%s)", errorRate);
        if (errorRate <= 0) {
            return getErrorRateRes(errorRate);
        } else if (errorRate >= 100) {
            throw new IllegalStateException(getErrorRateRes(errorRate).getMsg());
        } else if (random.nextFloat() <= new Float("0." + String.format("%02d", errorRate))) {
            throw new IllegalStateException(getErrorRateRes(errorRate).getMsg());
        }
        return getErrorRateRes(errorRate);
    }

    @HystrixCommand(fallbackMethod = "helloHystrixFallback")
    public Observable<Demo> helloHystrixObservable(final int errorRate) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(helloInternal(errorRate));
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            }
        });
    }

    public Demo helloHystrixFallback(int errorRate) {
        //setContext(contexts);
        print("helloHystrixFallback(%s)", errorRate);
        String res = "Fallback: " + getErrorRateRes(errorRate).getMsg();
        return new Demo(res, res);
    }

    private Demo getErrorRateRes(int errorRate) {
        String res = "Error Rate" + errorRate;
        return new Demo(res, res);
    }

    private void print(String msg, Object... args) {
        logger.info(Thread.currentThread().getName() + "(" + ServicesRequestContextHolder.get().getRequestID() + ") " +
                String.format(msg, args));
    }
}
