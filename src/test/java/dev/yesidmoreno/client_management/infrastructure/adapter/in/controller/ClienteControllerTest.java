package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.port.in.ClienteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteUseCase clienteUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNames("Test");
        cliente.setSurname("User");
        cliente.setEmail("test@user.com");
        cliente.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void createCliente() throws Exception {
        when(clienteUseCase.createCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/client")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated());
    }

    @Test
    void getCliente() throws Exception {
        when(clienteUseCase.getCliente(any(UUID.class))).thenReturn(cliente);

        mockMvc.perform(get("/client/{id}", UUID.randomUUID())
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk());
    }

    @Test
    void updateCliente() throws Exception {
        when(clienteUseCase.updateCliente(any(UUID.class), any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/client/{id}", UUID.randomUUID())
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCliente() throws Exception {
        doNothing().when(clienteUseCase).deleteCliente(any(UUID.class));

        mockMvc.perform(delete("/client/{id}", UUID.randomUUID())
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isNoContent());
    }
}