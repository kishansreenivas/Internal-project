package com.BookingService.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

//aop/LoggingAspect.java
@Aspect @Component
public class LoggingAspect {
private final Logger log = LoggerFactory.getLogger(this.getClass());

@Around("execution(* com.bookingservice..*(..))")
public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {
 String m = pjp.getSignature().toShortString();
 long start = System.nanoTime();
 Object res = pjp.proceed();
 log.info("{} executed in {} ms", m, (System.nanoTime()-start)/1_000_000);
 return res;
}
}
