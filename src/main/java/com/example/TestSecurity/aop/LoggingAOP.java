package com.example.TestSecurity.aop;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
public class LoggingAOP {

    public static final Logger log = LogManager.getLogger(LoggingAOP.class);

    @Pointcut("execution(* com.example.TestSecurity.controller..*.*(..))")
    private void cut() {}

    @Before("cut()")
    public void before(JoinPoint joinPoint) {

        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        java.lang.reflect.Method method = ms.getMethod();

        Object[] args = joinPoint.getArgs();

        log.info("==================== Before Method ==================");
        log.info("* information     : " + ms);
        log.info("* method name     : " + method);
        log.info("* parameter       : " + Arrays.toString(args));
        log.info("=====================================================");
    }

    @AfterReturning(value="cut()", returning="obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        // * 메소드 이름 추출
        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        java.lang.reflect.Method method = ms.getMethod();

        log.info("==================== After Return Method =================");
        log.info("* information : " + ms);
        log.info("* method name : " + method);
        log.info("* Object      : " + obj);
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object work = pjp.proceed();

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        log.info("-------------------- Around Method ----------------");
        log.info("* information     : " + pjp.getSignature());
        log.info("* processing time : " + time + "ms");

        return work;
    }
}
