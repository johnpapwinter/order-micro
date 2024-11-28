package com.example.logging.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private final ThreadLocal<Boolean> isLogging = new ThreadLocal<>();



    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Pointcut("execution(* org.springframework.data.mongodb.repository.MongoRepository+.*(..))")
    public void databaseOperations() {}

    @Around("restControllerMethods()")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        if (Boolean.TRUE.equals(isLogging.get())) {
            return joinPoint.proceed();
        }

        try {
            isLogging.set(true);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();

            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            log.info("[{}] REST Request - Method: {}, Path: {}", className, methodName, request.getRequestURI());

            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                log.error("[{}] REST Request - Method: {}, Path: {}, Exception: {}",
                        className, methodName, request.getRequestURI(), e.getMessage()
                );
                throw e;
            }
        } finally {
            isLogging.remove();
        }
    }

    @Around("databaseOperations()")
    public Object logDatabaseOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("[{}] Database Operation Start - Method: {}", className, methodName);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            log.info("[{}] Database Operation End - Method: {}, Duration: {} ms",
                    className, methodName, System.currentTimeMillis() - startTime
            );

            return result;
        } catch (Exception e) {
            log.error("[{}] Database Operation Error: Method: {}, Error: {}", className, methodName, e.getMessage());
            throw e;
        }
    }


}
