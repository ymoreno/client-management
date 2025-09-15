package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.TransaccionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepositoryPort transaccionRepositoryPort;

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @Mock
    private LogPort logPort;

    @InjectMocks
    private TransaccionService transaccionService;

    private Producto originAccount;
    private Producto targetAccount;

    @BeforeEach
    void setUp() {
        originAccount = new Producto();
        originAccount.setId(UUID.randomUUID());
        originAccount.setBalance(1000.0);

        targetAccount = new Producto();
        targetAccount.setId(UUID.randomUUID());
        targetAccount.setBalance(500.0);
    }

    @Test
    void doTransaccion_deposit_shouldThrowNotFoundException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            transaccionService.doTransaccion(UUID.randomUUID(), null, Transaccion.TransactionType.DEPOSIT, 100.0);
        });
    }

    @Test
    void doTransaccion_deposit_shouldDepositSuccessfully() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(originAccount));
        when(transaccionRepositoryPort.save(any(Transaccion.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaccion result = transaccionService.doTransaccion(originAccount.getId(), null, Transaccion.TransactionType.DEPOSIT, 100.0);

        assertNotNull(result);
        assertEquals(1100.0, originAccount.getBalance());
    }

    @Test
    void doTransaccion_withdrawal_shouldThrowNotFoundException_whenAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            transaccionService.doTransaccion(UUID.randomUUID(), null, Transaccion.TransactionType.WITHDRAWAL, 100.0);
        });
    }

    @Test
    void doTransaccion_withdrawal_shouldThrowValidationException_whenBalanceIsInsufficient() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(originAccount));

        assertThrows(ValidationException.class, () -> {
            transaccionService.doTransaccion(originAccount.getId(), null, Transaccion.TransactionType.WITHDRAWAL, 2000.0);
        });
    }

    @Test
    void doTransaccion_withdrawal_shouldWithdrawSuccessfully() {
        when(productoRepositoryPort.getProductoById(any(UUID.class))).thenReturn(Optional.of(originAccount));
        when(transaccionRepositoryPort.save(any(Transaccion.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaccion result = transaccionService.doTransaccion(originAccount.getId(), null, Transaccion.TransactionType.WITHDRAWAL, 100.0);

        assertNotNull(result);
        assertEquals(900.0, originAccount.getBalance());
    }

    @Test
    void doTransaccion_transfer_shouldThrowValidationException_whenAccountsAreTheSame() {
        UUID accountId = UUID.randomUUID();
        assertThrows(ValidationException.class, () -> {
            transaccionService.doTransaccion(accountId, accountId, Transaccion.TransactionType.TRANSFER, 100.0);
        });
    }

    @Test
    void doTransaccion_transfer_shouldThrowNotFoundException_whenOriginAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(originAccount.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            transaccionService.doTransaccion(originAccount.getId(), targetAccount.getId(), Transaccion.TransactionType.TRANSFER, 100.0);
        });
    }

    @Test
    void doTransaccion_transfer_shouldThrowNotFoundException_whenTargetAccountDoesNotExist() {
        when(productoRepositoryPort.getProductoById(originAccount.getId())).thenReturn(Optional.of(originAccount));
        when(productoRepositoryPort.getProductoById(targetAccount.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            transaccionService.doTransaccion(originAccount.getId(), targetAccount.getId(), Transaccion.TransactionType.TRANSFER, 100.0);
        });
    }

    @Test
    void doTransaccion_transfer_shouldThrowValidationException_whenBalanceIsInsufficient() {
        when(productoRepositoryPort.getProductoById(originAccount.getId())).thenReturn(Optional.of(originAccount));
        when(productoRepositoryPort.getProductoById(targetAccount.getId())).thenReturn(Optional.of(targetAccount));

        assertThrows(ValidationException.class, () -> {
            transaccionService.doTransaccion(originAccount.getId(), targetAccount.getId(), Transaccion.TransactionType.TRANSFER, 2000.0);
        });
    }

    @Test
    void doTransaccion_transfer_shouldTransferSuccessfully() {
        when(productoRepositoryPort.getProductoById(originAccount.getId())).thenReturn(Optional.of(originAccount));
        when(productoRepositoryPort.getProductoById(targetAccount.getId())).thenReturn(Optional.of(targetAccount));
        when(transaccionRepositoryPort.save(any(Transaccion.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaccion result = transaccionService.doTransaccion(originAccount.getId(), targetAccount.getId(), Transaccion.TransactionType.TRANSFER, 100.0);

        assertNotNull(result);
        assertEquals(900.0, originAccount.getBalance());
        assertEquals(600.0, targetAccount.getBalance());
    }
}
