package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.port.in.ProductoUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    @PostMapping("/checking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> createCheckingAccount(@RequestBody Producto producto) {
        return new ResponseEntity<>(productoUseCase.createCheckingAccount(producto), HttpStatus.CREATED);
    }

    @PostMapping("/savings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> createSavingsAccount(@RequestBody Producto producto) {
        return new ResponseEntity<>(productoUseCase.createSavingsAccount(producto), HttpStatus.CREATED);
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> activateAccount(@PathVariable UUID id) {
        return new ResponseEntity<>(productoUseCase.activateAccount(id), HttpStatus.OK);
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> deactivateAccount(@PathVariable UUID id) {
        return new ResponseEntity<>(productoUseCase.deactivateAccount(id), HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> cancelAccount(@PathVariable UUID id) {
        return new ResponseEntity<>(productoUseCase.cancelAccount(id), HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Producto> getProductoByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(productoUseCase.getProductoByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
