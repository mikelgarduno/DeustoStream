package com.example.restapi.integration;

import com.example.restapi.controller.MobileLoginController;
import com.example.restapi.service.QrLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MobileLoginController.class)
public class MobileLoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QrLoginService qrLoginService;

    @Test
    void autorizarQr_debeRetornarAutorizadoYMarcarToken() throws Exception {
        mockMvc.perform(post("/autorizar-qr")
                .param("token", "abc123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Autorizado"));

        verify(qrLoginService).marcarAutorizado("abc123");
    }
}
