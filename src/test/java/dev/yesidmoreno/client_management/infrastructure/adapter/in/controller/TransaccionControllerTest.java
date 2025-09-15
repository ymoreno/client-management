package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.in.TransaccionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransaccionUseCase transaccionUseCase;

    @Test
    void doTransaccion() throws Exception {
        when(transaccionUseCase.doTransaccion(any(), any(), any(), any())).thenReturn(new Transaccion());

        mockMvc.perform(post("/transaction")
                .with(user("user").roles("USER"))
                .param("originId", UUID.randomUUID().toString())
                .param("targetId", UUID.randomUUID().toString())
                .param("type", "DEPOSIT")
                .param("amount", "100.0"))
                .andExpect(status().isOk());
    }
}