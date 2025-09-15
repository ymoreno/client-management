package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.out.ClienteRepositoryPort;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @Mock
    private LogPort logPort;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNames("Test");
        cliente.setSurname("User");
        cliente.setEmail("test@user.com");
        cliente.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void createCliente_shouldThrowValidationException_whenClientIsNotAdult() {
        cliente.setBirthDate(LocalDate.now().minusYears(17));

        assertThrows(ValidationException.class, () -> {
            clienteService.createCliente(cliente);
        });
    }

    @Test
    void createCliente_shouldCreateCliente_whenClientIsAdult() {
        when(clienteRepositoryPort.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.createCliente(cliente);

        assertNotNull(result);
        assertEquals(cliente, result);
    }

    @Test
    void updateCliente_shouldThrowNotFoundException_whenClientDoesNotExist() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            clienteService.updateCliente(UUID.randomUUID(), cliente);
        });
    }

    @Test
    void updateCliente_shouldUpdateCliente_whenClientExists() {
        Cliente updatedCliente = new Cliente();
        updatedCliente.setNames("Updated");
        updatedCliente.setSurname("User");
        updatedCliente.setEmail("updated@user.com");

        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(cliente));
        when(clienteRepositoryPort.save(any(Cliente.class))).thenReturn(updatedCliente);

        Cliente result = clienteService.updateCliente(UUID.randomUUID(), updatedCliente);

        assertNotNull(result);
        assertEquals(updatedCliente.getNames(), result.getNames());
    }

    @Test
    void deleteCliente_shouldThrowNotFoundException_whenClientDoesNotExist() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            clienteService.deleteCliente(UUID.randomUUID());
        });
    }

    @Test
    void deleteCliente_shouldThrowValidationException_whenClientHasActiveProducts() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(cliente));
        when(productoRepositoryPort.existsByOwnerIdAndAccountStatus(any(UUID.class), any(Producto.AccountStatus.class))).thenReturn(true);

        assertThrows(ValidationException.class, () -> {
            clienteService.deleteCliente(UUID.randomUUID());
        });
    }

    @Test
    void deleteCliente_shouldDeleteCliente_whenClientHasNoActiveProducts() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(cliente));
        when(productoRepositoryPort.existsByOwnerIdAndAccountStatus(any(UUID.class), any(Producto.AccountStatus.class))).thenReturn(false);

        assertDoesNotThrow(() -> {
            clienteService.deleteCliente(UUID.randomUUID());
        });
    }

    @Test
    void getCliente_shouldThrowNotFoundException_whenClientDoesNotExist() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            clienteService.getCliente(UUID.randomUUID());
        });
    }

    @Test
    void getCliente_shouldReturnCliente_whenClientExists() {
        when(clienteRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getCliente(UUID.randomUUID());

        assertNotNull(result);
        assertEquals(cliente, result);
    }
}
