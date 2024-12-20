package com.system.tm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(
            pointcut = "@within(com.system.tm.aspect.log.LogError)",
            throwing = "ex"
    )
    public void logException(final JoinPoint joinPoint, final Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error(
                "Исключение в методе {} класса {}: {}",
                methodName,
                className,
                ex.getMessage()
        );
    }
}
