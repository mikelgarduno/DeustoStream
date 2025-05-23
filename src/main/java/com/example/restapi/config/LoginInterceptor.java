package com.example.restapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("usuario") != null);

        String uri = request.getRequestURI();

        // permite libre acceso a estas rutas:
        boolean accesolibre = uri.equals("/login") ||
                uri.equals("/registro") ||
                uri.equals("/qr-login") ||
                uri.equals("/qr-login-form") || 
                uri.equals("/qr-login-submit") ||
                uri.startsWith("/css/") || uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.startsWith("/index") || uri.startsWith("/styles.css");

        if (isLoggedIn || accesolibre) {
            return true;
        }

        response.sendRedirect("/login");
        return false;

    }

}
