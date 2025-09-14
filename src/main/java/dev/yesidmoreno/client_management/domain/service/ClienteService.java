package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.common.GeneralUtil;
import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ClienteUseCase;
import dev.yesidmoreno.client_management.domain.port.out.ClienteRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService implements ClienteUseCase {

    ClienteRepositoryPort clienteRepositoryPort;
    ProductoRepositoryPort productoRepositoryPort;

    @Override
    public Cliente createCliente(Cliente cliente) {
        if (!GeneralUtil.isAdult(cliente.getBirthDate())){
            throw new ValidationException("El cliente debe ser mayor de edad");
        }
        cliente.setCreationDate(LocalDateTime.now());
        cliente.setModificationDate(LocalDateTime.now());
        return clienteRepositoryPort.save(cliente);
    }

    @Override
    public Cliente updateCliente(UUID id, Cliente cliente) {
        Cliente existente = clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente a modificar"));

        existente.setNames(cliente.getNames());
        existente.setSurname(cliente.getSurname());
        existente.setEmail(cliente.getEmail());
        existente.setModificationDate(LocalDateTime.now());

        return clienteRepositoryPort.save(existente);
    }


    @Override
    public void deleteCliente(UUID id) {
        Cliente existente = clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente a eliminar"));
        if (productoRepositoryPort.existsByClienteIdAndEstado(existente.getId(), String.valueOf(Producto.AccountStatus.ACTIVE))){
            throw new ValidationException("No se puede eliminar un cliente con productos activos");
        }
        clienteRepositoryPort.deleteById(id);
    }

    @Override
    public Cliente getCliente(UUID id) {
        return clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente con el id proporcionado"));
    }
}