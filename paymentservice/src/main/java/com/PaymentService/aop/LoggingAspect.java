package com.PaymentService.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Log method entry
    @Before("execution(* com.PaymentService.Service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("ENTRY: {}() with arguments = {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    // Log method exit
    @AfterReturning(pointcut = "execution(* com.PaymentService.Service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("EXIT: {}() - Return value = {}", joinPoint.getSignature().getName(), result);
    }

    // Log exceptions
    @AfterThrowing(pointcut = "execution(* com.PaymentService.Service.*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("EXCEPTION in {}() - Message = {}", joinPoint.getSignature().getName(), ex.getMessage(), ex);
    }
}
