package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ProductoUseCase;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;

import java.util.UUID;

public class ProductoService implements ProductoUseCase {

    ProductoRepositoryPort productoRepositoryPort;

    @Override
    public Producto createCheckingAccount(Producto producto) {
        producto.setAccountNumber(AccountType.CA.getPrefix());
        return null;
    }

    @Override
    public Producto createSavingsAccount(Producto producto) {
        return null;
    }

    @Override
    public Producto activateAccount(UUID id) {
        return null;
    }

    @Override
    public Producto deactivateAccount(UUID id) {
        return null;
    }

    @Override
    public Producto cancelAccount(UUID id) {
        return null;
    }
}