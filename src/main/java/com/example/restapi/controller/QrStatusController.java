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

@RestController // Asegúrate de usar @RestController (combina @Controller y @ResponseBody)
public class QrStatusController {

    private final QrLoginService qrLoginService;

    @Autowired
    public QrStatusController(QrLoginService qrLoginService) {
        this.qrLoginService = qrLoginService;
    }

    @GetMapping("/qr-status")
    @ResponseBody // Asegúrate de que esta anotación esté presente
    public Map<String, Object> verificarEstadoToken(@RequestParam String token, HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        if (qrLoginService.estaAutorizado(token)) {
            Usuario usuario = qrLoginService.getUsuarioAutorizado(token);
            if (usuario != null) {
    
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioAutenticado", true);
                response.put("autorizado", true);
                response.put("usuario", usuario.getCorreo());
                return response;
            }
        } 

        response.put("autorizado", false);
        return response;
    }
}