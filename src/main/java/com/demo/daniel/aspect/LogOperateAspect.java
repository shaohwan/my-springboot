package com.demo.daniel.aspect;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.model.entity.LogOperate;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.LogStatus;
import com.demo.daniel.repository.LogOperateRepository;
import com.demo.daniel.util.ClientRequestUtils;
import com.demo.daniel.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class LogOperateAspect {

    @Autowired
    private LogOperateRepository logOperateRepository;

    @Around("@annotation(operateLog)")
    public Object logOperation(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {
        StopWatch stopwatch = StopWatch.createStarted();
        String username = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        HttpServletRequest request = ClientRequestUtils.getCurrentRequest();
        String ip = ClientRequestUtils.getClientIp(request);

        LogOperate logOperate = new LogOperate();
        logOperate.setModule(operateLog.module());
        logOperate.setName(operateLog.name());
        logOperate.setType(operateLog.type().length > 0 ? operateLog.type()[0] : LogOperateType.OTHER);
        logOperate.setCreateTime(LocalDateTime.now());
        logOperate.setUsername(username);
        logOperate.setIp(ip);
        logOperate.setAddress(ClientRequestUtils.getLocationByIp(ip));
        logOperate.setUserAgent(ClientRequestUtils.getUserAgent(request));
        logOperate.setRequestUri(request.getRequestURI());
        logOperate.setRequestMethod(request.getMethod());
        logOperate.setRequestParameters(getRequestParameters(request, joinPoint));

        // Execute the method and handle result
        try {
            Object result = joinPoint.proceed();
            logOperate.setStatus(LogStatus.SUCCESS);
            logOperate.setResultMessage("Operation successful");
            return result;
        } catch (Throwable throwable) {
            logOperate.setStatus(LogStatus.FAILURE);
            logOperate.setResultMessage(throwable.getMessage());
            throw throwable;
        } finally {
            // Set duration
            stopwatch.stop();
            logOperate.setDuration(stopwatch.getTime(TimeUnit.MILLISECONDS));
            // Save to database
            logOperateRepository.save(logOperate);
        }
    }

    private String getRequestParameters(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        Map<String, Object> parameterMap = new HashMap<>();

        request.getParameterMap().forEach((key, value) -> parameterMap.put(key, String.join(",", value)));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && !(args[i] instanceof HttpServletRequest)) {
                parameterMap.put(paramNames[i] != null ? paramNames[i] : "param" + i, args[i]);
            }
        }

        if (request instanceof ContentCachingRequestWrapper) {
            byte[] content = ((ContentCachingRequestWrapper) request).getContentAsByteArray();
            if (content.length > 0) {
                parameterMap.put("jsonBody", new String(content, StandardCharsets.UTF_8));
            }
        }

        if (request instanceof MultipartHttpServletRequest) {
            ((MultipartHttpServletRequest) request).getFileMap().forEach((key, file) -> parameterMap.put(key, file.getOriginalFilename()));
        }

        return JsonUtils.toJson(parameterMap);
    }
}
