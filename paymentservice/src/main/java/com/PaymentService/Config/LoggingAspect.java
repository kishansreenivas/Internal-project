package com.PaymentService.Config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.flixshow.paymentservice.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("➡️ Calling: " + joinPoint.getSignature().getName() +
            " | Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.flixshow.paymentservice.service.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("✅ Finished: " + joinPoint.getSignature().getName() +
            " | Returned: " + result);
    }
}
