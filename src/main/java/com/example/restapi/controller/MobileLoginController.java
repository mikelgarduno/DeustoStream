package com.example.restapi.controller;

import com.example.restapi.service.QrLoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileLoginController {

    @Autowired
    private QrLoginService qrLoginService;

    @PostMapping("/autorizar-qr")
    public String autorizarQr(@RequestParam String token, HttpSession session) {
        // Aquí podrías validar credenciales del usuario primero
        qrLoginService.marcarAutorizado(token);

        // También podrías iniciar sesión aquí con Spring Security o guardar usuario en la sesión
        session.setAttribute("usuarioAutenticado", true); // ejemplo

        return "Autorizado";
    }
}
