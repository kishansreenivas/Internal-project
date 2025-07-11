package com.BookingService.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Logs execution details for all public methods in BookingServiceImpl
     */
    @Around("execution(public * com.BookingService.Service.Impl.BookingServiceImpl.*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("➡️  START {}.{}() with args: {}", className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ END {}.{}(); Duration: {} ms", className, methodName, duration);
            return result;
        } catch (Exception ex) {
            log.error("❌ EXCEPTION in {}.{}(): {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}
