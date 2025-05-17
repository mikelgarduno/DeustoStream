package com.example.restapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QrLoginService {

    private final Map<String, Boolean> tokenAutorizado = new ConcurrentHashMap<>();

    public void registrarToken(String token) {
        tokenAutorizado.put(token, false);
    }

    public void marcarAutorizado(String token) {
        tokenAutorizado.put(token, true);
    }

    public boolean estaAutorizado(String token) {
        return tokenAutorizado.getOrDefault(token, false);
    }

    public byte[] generarCodigoQR(String contenido) throws Exception {
        int width = 300;
        int height = 300;

        BitMatrix matrix = new MultiFormatWriter()
                .encode(contenido, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}
