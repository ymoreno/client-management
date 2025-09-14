package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    ProductoJpaRepository productoJpaRepository;

    @Override
    public Producto save(Producto producto) {
        return productoJpaRepository.save(producto);
    }

    @Override
    public Producto changeStatus(UUID id, String status) {
        Optional<Producto> accountOpt = productoJpaRepository.findById(id);
        Producto account = accountOpt.get(); //la validación de existencia se hizo en el servicio
        try {
            Producto.AccountStatus newStatus = Producto.AccountStatus.valueOf(status.toUpperCase());
            account.setAccountStatus(newStatus);
            account.setModificationDate(LocalDateTime.now());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Estado inválido: " + status);
        }
        return productoJpaRepository.save(account);
    }

    @Override
    public boolean existsByClienteIdAndEstado(UUID clienteId, String status) {
        return productoJpaRepository.existsByClienteIdAndStatus(clienteId, status);
    }

    @Override
    public Optional<Producto> getProductoById(UUID id) {
        return productoJpaRepository.findById(id);
    }

    @Override
    public Optional<Producto> findByAccountNumber(String accountNumber) {
        return productoJpaRepository.findByAccountNumber(accountNumber);
    }
}
