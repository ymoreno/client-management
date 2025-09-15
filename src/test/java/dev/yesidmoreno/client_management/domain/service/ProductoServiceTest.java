package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.out.AccountNumberRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @Mock
    private AccountNumberRepositoryPort accountNumberRepository;

    @Mock
    private LogPort logPort;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        producto = new Producto();
        producto.setId(UUID.randomUUID());
        producto.setOwner(cliente);
        producto.setBalance(1000.0);
    }

    @Test
    void createCheckingAccount_shouldCreateAccount() {
        when(accountNumberRepository.generateAccountNumber(AccountType.CC)).thenReturn("3300000001");
        when(productoRepositoryPort.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.createCheckingAccount(producto);

        assertNotNull(result);
        assertEquals(Producto.AccountStatus.ACTIVE, result.getAccountStatus());
        assertEquals("3300000001", result.getAccountNumber());
    }

    @Test
    void createSavingsAccount_shouldCreateAccount() {
        when(accountNumberRepository.generateAccountNumber(AccountType.CA)).thenReturn("5300000001");
        when(productoRepositoryPort.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.createSavingsAccount(producto);

        assertNotNull(result);
        assertEquals("5300000001", result.getAccountNumber());
    }

    @Test
    void activateAccount_shouldThrowValidationException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> {
            productoService.activateAccount(UUID.randomUUID());
        });
    }

    @Test
    void activateAccount_shouldActivateAccount() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(producto));
        when(productoRepositoryPort.changeStatus(any(UUID.class), any(String.class))).thenReturn(producto);

        Producto result = productoService.activateAccount(UUID.randomUUID());

        assertNotNull(result);
    }

    @Test
    void deactivateAccount_shouldThrowValidationException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> {
            productoService.deactivateAccount(UUID.randomUUID());
        });
    }

    @Test
    void deactivateAccount_shouldDeactivateAccount() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(producto));
        when(productoRepositoryPort.changeStatus(any(UUID.class), any(String.class))).thenReturn(producto);

        Producto result = productoService.deactivateAccount(UUID.randomUUID());

        assertNotNull(result);
    }

    @Test
    void cancelAccount_shouldThrowValidationException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> {
            productoService.cancelAccount(UUID.randomUUID());
        });
    }

    @Test
    void cancelAccount_shouldThrowValidationException_whenBalanceIsNotZero() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(producto));

        assertThrows(ValidationException.class, () -> {
            productoService.cancelAccount(UUID.randomUUID());
        });
    }

    @Test
    void cancelAccount_shouldCancelAccount_whenBalanceIsZero() {
        producto.setBalance(0.0);
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(producto));
        when(productoRepositoryPort.changeStatus(any(UUID.class), any(String.class))).thenReturn(producto);

        Producto result = productoService.cancelAccount(UUID.randomUUID());

        assertNotNull(result);
    }

    @Test
    void getProductoByAccountNumber_shouldThrowNotFoundException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.findByAccountNumber(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            productoService.getProductoByAccountNumber("1234567890");
        });
    }

    @Test
    void getProductoByAccountNumber_shouldReturnProducto_whenAccountExists() {
        when(productoRepositoryPort.findByAccountNumber(any(String.class))).thenReturn(Optional.of(producto));

        Producto result = productoService.getProductoByAccountNumber("1234567890");

        assertNotNull(result);
        assertEquals(producto, result);
    }
}
