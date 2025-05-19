package com.example.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(!"test".equals(System.getProperty("spring.profiles.active"))) {
            // Si no estamos en el perfil de test, añadimos el interceptor
            registry.addInterceptor(new LoginInterceptor())
                    .addPathPatterns("/**")
                    .excludePathPatterns(
                            "/",
                            "/index",
                            "/login",
                            "/registro",
                            "/qr-login", // Asegúrate de que esta también esté excluida si la accedes sin sesión
                            "/qr-login-form", // También podría ser necesario excluirla
                            "/qr-login-submit", // También podría ser necesario excluirla
                            "/qr-status", // ¡Añade esta línea!
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/styles.css"
                    );
        }
    }
}