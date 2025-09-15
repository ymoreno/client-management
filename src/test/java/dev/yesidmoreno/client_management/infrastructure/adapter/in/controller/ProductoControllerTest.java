package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ProductoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoUseCase productoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(UUID.randomUUID());
    }

    @Test
    void createCheckingAccount() throws Exception {
        when(productoUseCase.createCheckingAccount(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/product/checking")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createSavingsAccount() throws Exception {
        when(productoUseCase.createSavingsAccount(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/product/savings")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated());
    }

    @Test
    void activateAccount() throws Exception {
        when(productoUseCase.activateAccount(any(UUID.class))).thenReturn(producto);

        mockMvc.perform(put("/product/activate/{id}", UUID.randomUUID())
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void deactivateAccount() throws Exception {
        when(productoUseCase.deactivateAccount(any(UUID.class))).thenReturn(producto);

        mockMvc.perform(put("/product/deactivate/{id}", UUID.randomUUID())
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void cancelAccount() throws Exception {
        when(productoUseCase.cancelAccount(any(UUID.class))).thenReturn(producto);

        mockMvc.perform(put("/product/cancel/{id}", UUID.randomUUID())
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getProductoByAccountNumber() throws Exception {
        when(productoUseCase.getProductoByAccountNumber(any(String.class))).thenReturn(producto);

        mockMvc.perform(get("/product/{accountNumber}", "1234567890")
                .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }
}