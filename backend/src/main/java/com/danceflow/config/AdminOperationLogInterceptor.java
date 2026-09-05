package com.danceflow.config;

import com.danceflow.security.AuthUser;
import com.danceflow.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminOperationLogInterceptor implements HandlerInterceptor {
    private final OperationLogService logService;
    public AdminOperationLogInterceptor(OperationLogService logService) { this.logService = logService; }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        if (!request.getRequestURI().startsWith("/api/admin/") || "GET".equalsIgnoreCase(request.getMethod())) return;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUser user)) return;
        String result = exception == null && response.getStatus() < 400 ? "SUCCESS" : "FAILED";
        String targetId = request.getRequestURI().replaceFirst(".*/", "");
        logService.record(user.id(), user.username(), request.getMethod(), request.getRequestURI(), result, "target=" + targetId);
    }
}
