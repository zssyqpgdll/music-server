package com.zhang.recommendation_system.config;


import com.zhang.recommendation_system.pojo.admin.Log;
import com.zhang.recommendation_system.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private LogService logService;

    //@Pointcut("execution(* com.university.demo.controller.*.*(..))")
    @Pointcut("@annotation(com.zhang.recommendation_system.config.SysLog)")
    public void webLog(){}

//    @Around("execution(* com.lyk.coursearrange.controller.*.*(..))")
//    public ServerResponse exceptionAop(ProceedingJoinPoint pj) throws Throwable{
//        ServerResponse response = null;
//        try{
//            return (ServerResponse) pj.proceed();
//        }catch (Exception e){
//            logger.error("出现运行时异常：", e);
//            response = ServerResponse.ofError(e);
//        }
//        return response;
//    }

    /**
     * 前置通知：在连接点之前执行的通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        SysLog syslog = method.getAnnotation(SysLog.class);

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        Log log = new Log();
        log.setOpt(syslog.value());
        log.setUrl(request.getRequestURL().toString());
        log.setHttpMethod(request.getMethod());
        log.setIp(request.getRemoteAddr());
        log.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.setArgs(Arrays.toString(joinPoint.getArgs()));
        logService.save(log);
    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }
}
