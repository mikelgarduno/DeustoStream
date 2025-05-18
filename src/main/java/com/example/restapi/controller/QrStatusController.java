package com.example.restapi.controller;

import com.example.restapi.model.Usuario;
import com.example.restapi.service.QrLoginService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QrStatusController {

    private final QrLoginService qrLoginService;

    @Autowired
    public QrStatusController(QrLoginService qrLoginService) {
        this.qrLoginService = qrLoginService;
    }

    @GetMapping("/qr-status")
    public Map<String, Object> verificarEstadoToken(@RequestParam String token, HttpSession session) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.info("Verificando estado del token QR: {}", token);

        Map<String, Object> response = new HashMap<>();

        if (qrLoginService.estaAutorizado(token)) {
            Usuario usuario = qrLoginService.getUsuarioAutorizado(token);
            if (usuario != null) {
                logger.info("Token autorizado para usuario: {}", usuario.getCorreo());

                // Guardar usuario en sesión para login persistente
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioAutenticado", true);

                response.put("autorizado", true);
                response.put("usuario", usuario.getCorreo());
                return response;
            } else {
                logger.warn("Token autorizado pero sin usuario asociado: {}", token);
            }
        } else {
            logger.info("Token no autorizado aún: {}", token);
        }

        response.put("autorizado", false);
        return response;
    }

}
