package dev.yesidmoreno.client_management.domain.port.in;

import dev.yesidmoreno.client_management.domain.model.Transaccion;

import java.util.UUID;

public interface TransaccionUseCase {
    Transaccion doTransaccion(UUID originId, UUID targetId, Transaccion.TransactionType type, Double amount);
}