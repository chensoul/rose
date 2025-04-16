package com.chensoul.rose.syslog.aspect;

import com.chensoul.rose.core.spring.SpringContextHolder;
import com.chensoul.rose.syslog.annotation.SysLog;
import com.chensoul.rose.syslog.event.SysLogEvent;
import com.chensoul.rose.syslog.event.SysLogInfo;
import com.chensoul.rose.syslog.util.SysLogUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 操作日志使用spring event异步入库
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class SysLogAspect {

    @SneakyThrows
    @Around("@annotation(sysLog)")
    public Object around(ProceedingJoinPoint joinPoint, SysLog sysLog) {
        String strClassName = joinPoint.getTarget().getClass().getName();
        String strMethodName = joinPoint.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        SysLogInfo sysLogInfo = SysLogUtils.getSysLog(joinPoint, sysLog);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            sysLogInfo.setException(e.getMessage());
            sysLogInfo.setSuccess(false);
            throw e;
        } finally {
            sysLogInfo.setCostTime(System.currentTimeMillis() - startTime);
            SpringContextHolder.publishEvent(new SysLogEvent(sysLogInfo));
        }
        return result;
    }
}
