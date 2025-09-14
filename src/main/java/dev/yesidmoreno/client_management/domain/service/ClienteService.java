package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.common.GeneralUtil;
import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ClienteUseCase;
import dev.yesidmoreno.client_management.domain.port.out.ClienteRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService implements ClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;
    private final LogPort logPort;

    @Override
    public Cliente createCliente(Cliente cliente) {
        logPort.info("Creando cliente: " + cliente.getNames());
        if (!GeneralUtil.isAdult(cliente.getBirthDate())) {
            logPort.error("Error al crear cliente: El cliente debe ser mayor de edad");
            throw new ValidationException("El cliente debe ser mayor de edad");
        }
        cliente.setCreationDate(LocalDateTime.now());
        cliente.setModificationDate(LocalDateTime.now());
        Cliente createdCliente = clienteRepositoryPort.save(cliente);
        logPort.info("Cliente creado exitosamente: " + createdCliente.getId());
        return createdCliente;
    }

    @Override
    public Cliente updateCliente(UUID id, Cliente cliente) {
        logPort.info("Actualizando cliente: " + id);
        Cliente existente = clienteRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    logPort.error("Error al actualizar cliente: No se encuentra el cliente a modificar con id: " + id);
                    return new NotFoundException("No se encuentra el cliente a modificar");
                });

        existente.setNames(cliente.getNames());
        existente.setSurname(cliente.getSurname());
        existente.setEmail(cliente.getEmail());
        existente.setModificationDate(LocalDateTime.now());

        Cliente updatedCliente = clienteRepositoryPort.save(existente);
        logPort.info("Cliente actualizado exitosamente: " + updatedCliente.getId());
        return updatedCliente;
    }


    @Override
    public void deleteCliente(UUID id) {
        logPort.info("Eliminando cliente: " + id);
        Cliente existente = clienteRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    logPort.error("Error al eliminar cliente: No se encuentra el cliente a eliminar con id: " + id);
                    return new NotFoundException("No se encuentra el cliente a eliminar");
                });
        if (productoRepositoryPort.existsByClienteIdAndEstado(existente.getId(), String.valueOf(Producto.AccountStatus.ACTIVE))) {
            logPort.error("Error al eliminar cliente: No se puede eliminar un cliente con productos activos");
            throw new ValidationException("No se puede eliminar un cliente con productos activos");
        }
        clienteRepositoryPort.deleteById(id);
        logPort.info("Cliente eliminado exitosamente: " + id);
    }

    @Override
    public Cliente getCliente(UUID id) {
        logPort.info("Buscando cliente: " + id);
        return clienteRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    logPort.error("Error al buscar cliente: No se encuentra el cliente con el id proporcionado: " + id);
                    return new NotFoundException("No se encuentra el cliente con el id proporcionado");
                });
    }
}
