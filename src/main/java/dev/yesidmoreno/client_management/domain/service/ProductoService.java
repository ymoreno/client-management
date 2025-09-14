package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ProductoUseCase;
import dev.yesidmoreno.client_management.domain.port.out.AccountNumberRepositoryPort;
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

    @Override
    public Producto createCheckingAccount(Producto producto) {
        String accountNumber = accountNumberRepository.generateAccountNumber(AccountType.CC);
        producto.setAccountStatus(Producto.AccountStatus.ACTIVE);
        producto.setAccountNumber(accountNumber);
        producto.setCreationDate(LocalDateTime.now());
        producto.setModificationDate(LocalDateTime.now());
        return productoRepositoryPort.save(producto);
    }

    @Override
    public Producto createSavingsAccount(Producto producto) {
        String accountNumber = accountNumberRepository.generateAccountNumber(AccountType.CA);
        producto.setAccountNumber(accountNumber);
        producto.setCreationDate(LocalDateTime.now());
        producto.setModificationDate(LocalDateTime.now());
        return productoRepositoryPort.save(producto);
    }

    @Override
    public Producto activateAccount(UUID id) {
        Optional<Producto> productToBeActivated = productoRepositoryPort.getProductoById(id);
        if (productToBeActivated.isEmpty()){
            throw new ValidationException("Cuenta no encontrada");
        }
        return productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.ACTIVE));
    }

    @Override
    public Producto deactivateAccount(UUID id) {
        Optional<Producto> productToBeDeActivated = productoRepositoryPort.getProductoById(id);
        if (productToBeDeActivated.isEmpty()){
            throw new ValidationException("Cuenta no encontrada");
        }
        return productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.INACTIVE));
    }

    @Override
    public Producto cancelAccount(UUID id) {
        Optional<Producto> productToBeCanceled = productoRepositoryPort.getProductoById(id);
        if (productToBeCanceled.isEmpty()){
            throw new ValidationException("Cuenta no encontrada");
        } else if (productToBeCanceled.get().getBalance()<=0) {
            throw new ValidationException("Para cancelar una cuenta su saldo debe ser $0.");
        }
        return productoRepositoryPort.changeStatus(id, String.valueOf(Producto.AccountStatus.CANCELLED));
    }
}