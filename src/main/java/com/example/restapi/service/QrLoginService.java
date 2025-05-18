package com.example.restapi.service;

import com.example.restapi.model.Usuario;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QrLoginService {
    // Mapa para almacenar tokens y su estado de autorización
    private final Map<String, QrLoginStatus> tokens = new ConcurrentHashMap<>();
    
    // Clase interna para mantener el estado completo del token
    private static class QrLoginStatus {
        private boolean autorizado = false;
        private Usuario usuario = null;
    }
    
    public void registrarToken(String token) {
        tokens.put(token, new QrLoginStatus());
        // Programar limpieza de tokens antiguos (opcional)
        programarLimpieza(token);
    }
    
    public boolean estaAutorizado(String token) {
        QrLoginStatus status = tokens.get(token);
        return status != null && status.autorizado;
    }
    
    public void marcarAutorizado(String token, Usuario usuario) {
        QrLoginStatus status = tokens.get(token);
        if (status != null) {
            status.autorizado = true;
            status.usuario = usuario;
        }
    }
    
    public Usuario getUsuarioAutorizado(String token) {
        QrLoginStatus status = tokens.get(token);
        if (status != null && status.autorizado) {
            return status.usuario;
        }
        return null;
    }
    
    public byte[] generarCodigoQR(String contenido) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, 250, 250);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    
    private void programarLimpieza(String token) {
        // Programar la eliminación del token después de 5 minutos
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tokens.remove(token);
            }
        }, 5 * 60 * 1000);
    }
}

