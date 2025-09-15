package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.Producto;

import java.util.Optional;
import java.util.UUID;

public interface ProductoRepositoryPort {

    Producto save(Producto producto);

    Producto changeStatus(UUID id, String status);

    boolean existsByOwnerIdAndAccountStatus(UUID ownerId, Producto.AccountStatus status);

    Optional<Producto> getProductoById(UUID id);

    Optional<Producto> findByAccountNumber(String accountNumber);

}