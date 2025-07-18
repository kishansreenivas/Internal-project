package com.UserService.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut for methods to log
	@Pointcut("execution(* com.UserService.Servicesimpl.UserServiceImpl.*(..))")

    public void userServiceMethods() {

	}

    // Log method entry
    @Before("userServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("➡️ Entering method: {} with arguments: {}", joinPoint
        		.getSignature()
        		.toShortString(),
        		joinPoint.getArgs());
    }

    // Log method exit
    @AfterReturning(pointcut = "userServiceMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("⬅️ Exiting method: {} with result: {}",
        		joinPoint.getSignature()
        		.toShortString(), result);
    }

    // Log exceptions
    @AfterThrowing(pointcut = "userServiceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        log.error("❌ Exception in method: {} with message: {}",
        		joinPoint.getSignature()
        		.toShortString(), ex.getMessage(), ex);
    }

    // Log execution time
    @Around("userServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("⏱️ Method {} executed in {} ms",
            		joinPoint.getSignature()
            		.toShortString(), totalTime);
        }
    }
}
