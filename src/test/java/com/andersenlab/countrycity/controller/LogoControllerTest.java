package com.andersenlab.countrycity.controller;

import com.andersenlab.countrycity.service.LogoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogoController.class)
@AutoConfigureMockMvc(addFilters = false)
class LogoControllerTest {
    @MockBean
    private LogoService logoService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Positive test for request logo")
    void getLogoById() throws Exception {
        when(logoService.getImageAsByteArray(any())).thenReturn("test".getBytes());

        mockMvc.perform(get("/logos/" + UUID.randomUUID().toString()))
                .andExpect(status().isOk());

        verify(logoService, times(1)).getImageAsByteArray(any());
    }

    @Test
    @DisplayName("Positive test for update logo image")
    void getChangeLogo() throws Exception {
        MockMultipartFile file = new MockMultipartFile("logo", "logo.jpg", "image/jpeg", "logo image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/logos/" + UUID.randomUUID().toString())
                        .file(file))
                .andExpect(status().isOk());

        verify(logoService, times(1)).updateLogo(any(), eq(file));
    }
}