package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ProductoUseCase;
import dev.yesidmoreno.client_management.domain.port.out.AccountNumberRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductoService implements ProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final AccountNumberRepositoryPort accountNumberRepository;
    private final LogPort logPort;

    @Override
    public Producto createCheckingAccount(Producto producto) {
        logPort.info("Creando cuenta de ahorros para el cliente: " + producto.getOwner().getId());
        String accountNumber = accountNumberRepository.generateAccountNumber(AccountType.CC);
        producto.setAccountStatus(Producto.AccountStatus.ACTIVE);
        producto.setAccountNumber(accountNumber);
        producto.setCreationDate(LocalDateTime.now());
        producto.setModificationDate(LocalDateTime.now());
        Producto createdProducto = productoRepositoryPort.save(producto);
        logPort.info("Cuenta de ahorros creada exitosamente: " + createdProducto.getId());
        return createdProducto;
    }

    @Override
    public Producto createSavingsAccount(Producto producto) {
        logPort.info("Creando cuenta corriente para el cliente: " + producto.getOwner().getId());
        String accountNumber = accountNumberRepository.generateAccountNumber(AccountType.CA);
        producto.setAccountNumber(accountNumber);
        producto.setCreationDate(LocalDateTime.now());
        producto.setModificationDate(LocalDateTime.now());
        Producto createdProducto = productoRepositoryPort.save(producto);
        logPort.info("Cuenta corriente creada exitosamente: " + createdProducto.getId());
        return createdProducto;
    }

    @Override
    public Producto activateAccount(UUID id) {
        logPort.info("Activando cuenta: " + id);
        Optional<Producto> productToBeActivated = productoRepositoryPort.getProductoById(id);
        if (productToBeActivated.isEmpty()) {
            logPort.error("Error al activar cuenta: Cuenta no encontrada con id: " + id);
            throw new ValidationException("Cuenta no encontrada");
        }
        Producto activatedProducto = productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.ACTIVE));
        logPort.info("Cuenta activada exitosamente: " + activatedProducto.getId());
        return activatedProducto;
    }

    @Override
    public Producto deactivateAccount(UUID id) {
        logPort.info("Desactivando cuenta: " + id);
        Optional<Producto> productToBeDeActivated = productoRepositoryPort.getProductoById(id);
        if (productToBeDeActivated.isEmpty()) {
            logPort.error("Error al desactivar cuenta: Cuenta no encontrada con id: " + id);
            throw new ValidationException("Cuenta no encontrada");
        }
        Producto deactivatedProducto = productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.INACTIVE));
        logPort.info("Cuenta desactivada exitosamente: " + deactivatedProducto.getId());
        return deactivatedProducto;
    }

    @Override
    public Producto cancelAccount(UUID id) {
        logPort.info("Cancelando cuenta: " + id);
        Optional<Producto> productToBeCanceled = productoRepositoryPort.getProductoById(id);
        if (productToBeCanceled.isEmpty()) {
            logPort.error("Error al cancelar cuenta: Cuenta no encontrada con id: " + id);
            throw new ValidationException("Cuenta no encontrada");
        } else if (productToBeCanceled.get().getBalance() != 0) {
            logPort.error("Error al cancelar cuenta: Para cancelar una cuenta su saldo debe ser $0.");
            throw new ValidationException("Para cancelar una cuenta su saldo debe ser $0.");
        }
        Producto cancelledProducto = productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.CANCELLED));
        logPort.info("Cuenta cancelada exitosamente: " + cancelledProducto.getId());
        return cancelledProducto;
    }

    @Override
    public Producto getProductoByAccountNumber(String accountNumber) {
        logPort.info("Buscando producto por número de cuenta: " + accountNumber);
        return productoRepositoryPort.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    logPort.error("Error al buscar producto: No se encuentra el producto con el número de cuenta proporcionado: " + accountNumber);
                    return new NotFoundException("No se encuentra el producto con el número de cuenta proporcionado");
                });
    }
}
