package com.linktic.productos.infraestructure.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logController(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final String className = signature.getDeclaringType().getSimpleName();
        final String methodName = signature.getName();
        final Object[] args = joinPoint.getArgs();

        log.info("[Controller] {}.{} - Entrada: {}", className, methodName, Arrays.toString(args));
        final Object result = joinPoint.proceed();
        log.info("[Controller] {}.{} - Respuesta: {}", className, methodName, result);

        return result;
    }
}
