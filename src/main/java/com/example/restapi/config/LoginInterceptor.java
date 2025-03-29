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

        boolean isLoggedIn =  (session != null && session.getAttribute("usuario") != null);

        String uri = request.getRequestURI();

        //permite libre acceso a estas rutas:
        boolean accesolibre = uri.equals("/login") ||
                                uri.equals("/registro") ||
                                uri.startsWith("/css") || uri.startsWith("/js") ||
                                uri.startsWith("/images");

        if (isLoggedIn || accesolibre) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
        
    }

 
}
