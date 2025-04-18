/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
