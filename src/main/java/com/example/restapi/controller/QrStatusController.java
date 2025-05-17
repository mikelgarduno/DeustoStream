package com.example.restapi.controller;

import com.example.restapi.service.QrLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QrStatusController {

    @Autowired
    private QrLoginService qrLoginService;

    @GetMapping("/qr-status")
    public boolean verificarEstadoToken(@RequestParam String token) {
        return qrLoginService.estaAutorizado(token);
    }
}
