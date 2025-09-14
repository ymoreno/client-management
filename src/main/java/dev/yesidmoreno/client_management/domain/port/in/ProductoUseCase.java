package dev.yesidmoreno.client_management.domain.port.in;

import dev.yesidmoreno.client_management.domain.model.Producto;

import java.util.UUID;

public interface ProductoUseCase {
    Producto createCheckingAccount(Producto producto);
    Producto createSavingsAccount(Producto producto);
    Producto activateAccount(UUID id);
    Producto deactivateAccount(UUID id);
    Producto cancelAccount(UUID id);
}