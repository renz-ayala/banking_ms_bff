package com.banking.ms.bankingmsbff.aspect;

import com.banking.ms.bankingmsbff.config.TrackingIdFilter;
import com.banking.ms.bankingmsbff.util.MdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Around("execution(* com.banking.ms.bankingmsbff.service.implementation.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        return ((Mono<?>) joinPoint.proceed())
                .contextWrite(ctx -> {
                    MdcUtil.map(ctx);
                    log.info("AOP Log - starting method : {}()", methodName);
                    return ctx;
                })
                .flatMap(response -> Mono.deferContextual(ctx -> {
                    String id = ctx.getOrDefault(TrackingIdFilter.TRACK_ID, "No-ID");
                    log.info("AOP Log [ID:{}] - method : {}() finalized successfully", id, methodName);
                    return Mono.just(response);
                }))
                .doOnError(throwable -> log.error("AOP Log - method : {}() failed", methodName));
    }
}