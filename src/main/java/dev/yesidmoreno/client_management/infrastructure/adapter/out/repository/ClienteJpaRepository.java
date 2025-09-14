package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteJpaRepository extends JpaRepository<Cliente, UUID> {
}