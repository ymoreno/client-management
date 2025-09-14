package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.Cliente;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepositoryPort {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(UUID id);

    void deleteById(UUID id);
}