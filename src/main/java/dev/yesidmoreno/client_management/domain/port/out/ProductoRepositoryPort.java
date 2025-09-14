package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.model.Producto;

import java.util.Optional;
import java.util.UUID;

public interface ProductoRepositoryPort {

    Producto save(Producto producto);
    Producto changeStatus(UUID id, String status);
    boolean existsByClienteIdAndEstado(UUID clienteId, String status);
    Optional<Producto> getProductoById(UUID id);

}