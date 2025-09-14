package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductoJpaRepository extends JpaRepository<Producto, UUID> {

    boolean existsByClienteIdAndStatus(UUID clienteId, String status);

}