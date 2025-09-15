package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import dev.yesidmoreno.client_management.domain.model.Cliente;
import dev.yesidmoreno.client_management.domain.port.in.ClienteUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteUseCase.createCliente(cliente), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> getCliente(@PathVariable UUID id) {
        return new ResponseEntity<>(clienteUseCase.getCliente(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> updateCliente(@PathVariable UUID id, @RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteUseCase.updateCliente(id, cliente), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable UUID id) {
        clienteUseCase.deleteCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
