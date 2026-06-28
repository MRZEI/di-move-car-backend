package com.dimovecar.interceptor;

import com.dimovecar.common.Result;
import com.dimovecar.common.ResultCode;
import com.dimovecar.common.UserContext;
import com.dimovecar.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String header = jwtUtil.getHeader();
        String prefix = jwtUtil.getPrefix();
        String authHeader = request.getHeader(header);

        if (authHeader == null || authHeader.isEmpty()) {
            writeUnauthorized(response);
            return false;
        }

        String token = authHeader;
        if (authHeader.startsWith(prefix + " ")) {
            token = authHeader.substring(prefix.length() + 1);
        }

        if (!jwtUtil.validateToken(token)) {
            writeUnauthorized(response);
            return false;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            writeUnauthorized(response);
            return false;
        }

        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            Result<Void> result = Result.fail(ResultCode.UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            log.error("写入未授权响应失败", e);
        }
    }
}
