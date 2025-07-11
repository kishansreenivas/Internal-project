package com.NotificationService.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {


    @Before("execution(* com.NotificationService.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[LOG] Entering: " + joinPoint.getSignature());
    }

    @AfterReturning("execution(* com.NotificationService.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("[LOG] Exiting: " + joinPoint.getSignature());
    }

}