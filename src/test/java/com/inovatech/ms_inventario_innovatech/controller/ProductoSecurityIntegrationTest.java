package com.inovatech.ms_inventario_innovatech.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProductos_esPublico() throws Exception {
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void crearProducto_sinAutenticacion_retorna403SegunConfiguracionActual() throws Exception {
        mockMvc.perform(post("/api/v1/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Producto QA",
                                  "descripcion": "Descripcion QA",
                                  "precio": 19990,
                                  "stock": 4,
                                  "categoria": "Testing"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@innovatech.cl", roles = "ADMIN")
    void crearProducto_conPayloadInvalido_retorna400YValidaciones() throws Exception {
        mockMvc.perform(post("/api/v1/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "descripcion": "",
                                  "precio": -1,
                                  "stock": -2,
                                  "categoria": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Solicitud invalida"))
                .andExpect(jsonPath("$.validations.nombre").exists())
                .andExpect(jsonPath("$.validations.descripcion").exists())
                .andExpect(jsonPath("$.validations.precio").exists())
                .andExpect(jsonPath("$.validations.stock").exists())
                .andExpect(jsonPath("$.validations.categoria").exists());
    }
}
