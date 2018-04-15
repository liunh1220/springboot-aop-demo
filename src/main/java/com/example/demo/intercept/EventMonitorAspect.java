package com.example.demo.intercept;

import com.example.demo.annotation.EventMonitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class EventMonitorAspect {

    private Logger logger = LoggerFactory.getLogger("monitor");

    @Pointcut("@annotation(com.example.demo.annotation.EventMonitor)")
    public void monitor() {
    }

    @Around("monitor()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EventMonitor monitor = method.getAnnotation(EventMonitor.class);

        String monitorName = monitor.name();
        String monitorDesc = monitor.desc();

        long start = System.currentTimeMillis();
        int status = 200;
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            status = 500;
            throw e;
        } finally {
            logger.info("{\"eventName\":\"{}\",\"eventDesc\":\"{}\",\"time\":{},\"status\":{}}", monitorName, monitorDesc, System.currentTimeMillis() - start, status);
        }
    }
}
