package dev.yesidmoreno.client_management.domain.port.in;

import dev.yesidmoreno.client_management.domain.model.Cliente;

import java.util.UUID;

public interface ClienteUseCase {
    Cliente createCliente(Cliente cliente);

    Cliente updateCliente(UUID id, Cliente cliente);

    void deleteCliente(UUID id);

    Cliente getCliente(UUID id);
}