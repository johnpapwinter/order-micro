package com.example.orders.aspect;

import com.example.orders.dto.LogMessageDTO;
import com.example.orders.feign.LoggingFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final LoggingFeignClient loggingFeignClient;

    public LoggingAspect(LoggingFeignClient loggingFeignClient) {
        this.loggingFeignClient = loggingFeignClient;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void restEndpoints() {}

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public void databaseOperations() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void exceptionHandling() {}

    @Around("exceptionHandling()")
    public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
        Exception exception = null;
        String exceptionType = "";

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Exception) {
                exception = (Exception) arg;
                exceptionType = exception.getClass().getSimpleName();
                break;
            }
        }

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        String requestPath = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
            requestPath = request.getRequestURI();
        } catch (IllegalStateException e) {
            requestPath = "N/A";
        }

        StringBuilder errorMessageBuilder = new StringBuilder()
                .append("Exception handled - Type: ").append(exceptionType)
                .append(", Path: ").append(requestPath);

        // Add validation errors if present
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) exception;
            List<String> errors = new ArrayList<>();
            validationException.getBindingResult().getAllErrors().forEach(error ->
                    errors.add(error.getDefaultMessage())
            );
            errorMessageBuilder.append(", Validation errors: ").append(String.join("; ", errors));
        } else if (exception != null) {
            errorMessageBuilder.append(", Message: ").append(exception.getMessage());
        }

        // Log the error
        LogMessageDTO errorLog = LogMessageDTO.builder()
                .timestamp(Instant.now())
                .thread(Thread.currentThread().getName())
                .level("ERROR")
                .logger(className)
                .message(errorMessageBuilder.toString())
                .build();

        sendLog(errorLog);

        // Proceed with the exception handler
        Object result = joinPoint.proceed();

        // Log the response
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        String responseBody = responseEntity.getBody() != null ? responseEntity.getBody().toString() : "null";

        LogMessageDTO responseLog = LogMessageDTO.builder()
                .timestamp(Instant.now())
                .thread(Thread.currentThread().getName())
                .level("INFO")
                .logger(className)
                .message(String.format("Exception handled - Status: %s, Response: %s",
                        responseEntity.getStatusCode(),
                        responseBody))
                .build();

        sendLog(responseLog);

        return result;
    }

    @Around("restEndpoints()")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        LogMessageDTO logMessageDTO = LogMessageDTO.builder()
                .timestamp(Instant.now())
                .thread(Thread.currentThread().getName())
                .level("INFO")
                .logger(className)
                .message(String.format("REST Request - Method: %s, Path: %s", methodName, request.getRequestURI()))
                .build();

        sendLog(logMessageDTO);
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            LogMessageDTO errorLog = LogMessageDTO.builder()
                    .timestamp(Instant.now())
                    .thread(Thread.currentThread().getName())
                    .level("ERROR")
                    .logger(className)
                    .message(String.format("REST Request - Method: %s, Path: %s", methodName, request.getRequestURI()))
                    .build();

            sendLog(errorLog);
            throw e;
        }
    }

    @Around("databaseOperations()")
    public Object logDatabaseOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        LogMessageDTO logMessageDTO = LogMessageDTO.builder()
                .timestamp(Instant.now())
                .thread(Thread.currentThread().getName())
                .level("DEBUG")
                .logger(className)
                .message(String.format("Database Operation Start - Method: %s", methodName))
                .build();

        sendLog(logMessageDTO);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();

            LogMessageDTO successLog = LogMessageDTO.builder()
                    .timestamp(Instant.now())
                    .thread(Thread.currentThread().getName())
                    .level("DEBUG")
                    .logger(className)
                    .message(String.format("Database Operation Success - Method: %s, Duration: %s",
                            methodName, System.currentTimeMillis() - startTime))
                    .build();

            sendLog(successLog);
            return result;
        } catch (Exception e) {
            LogMessageDTO errorLog = LogMessageDTO.builder()
                    .timestamp(Instant.now())
                    .thread(Thread.currentThread().getName())
                    .level("ERROR")
                    .logger(className)
                    .message(String.format("Database Operation Error - Method: %s, Error: %s", methodName, e.getMessage()))
                    .build();

            sendLog(errorLog);
            throw e;
        }
    }


    private void sendLog(LogMessageDTO logMessageDTO) {
        try {
            loggingFeignClient.storeLog(logMessageDTO);
        } catch (Exception e) {
            log.error("Failed to send log to logging service: {}", e.getMessage());
            log.info("Fallback: {}", logMessageDTO.getMessage());
        }
    }

}
