package com.tams.websocket.audit;

import com.tams.websocket.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Aspect
@Slf4j
public class TraceAspect {

    @Autowired
    private CaptureAuditData captureAuditData;

    @Pointcut("@annotation(Trace)")
    public void traceMethods() {
    }

    @Around("traceMethods()")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = Utilities.getCurrentTimestampInMills();
        Object returnValue = joinPoint.proceed();
        long executionTime = Utilities.getCurrentTimestampInMills() - startTime;

        AtomicReference<TraceInfo> traceInfo = new AtomicReference<>();
        Object[] methodArgs = joinPoint.getArgs();
        methodArgs = Arrays.stream(methodArgs).filter(arg -> {
            if (arg instanceof TraceInfo) {
                traceInfo.set((TraceInfo) arg);
                return false; // Exclude TraceInfo from the methodArgs array
            }
            return true; // Keep other arguments
        }).toArray();

        if(traceInfo.get() != null) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String fullQualifiedMethodName = methodSignature.getDeclaringType().getSimpleName()
                    + "." + methodSignature.getMethod().getName();

            Class<?> returnType = methodSignature.getReturnType();
            captureAuditData.captureAuditDataInDatabase(traceInfo.get(), fullQualifiedMethodName, startTime,
                    executionTime,
                    Utilities.object2JsonString(methodArgs),
                    (returnType.equals(void.class) ? "Void" : Utilities.object2JsonString(returnValue)));
        }
        return returnValue;
    }

}