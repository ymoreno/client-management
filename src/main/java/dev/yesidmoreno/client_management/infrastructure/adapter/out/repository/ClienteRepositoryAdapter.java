package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.port.out.ClienteRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    ClienteJpaRepository clienteJpaRepository;

    @Override
    public Cliente save(Cliente cliente) {
        return clienteJpaRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> findById(UUID id) {
        return clienteJpaRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        clienteJpaRepository.deleteById(id);
    }
}
