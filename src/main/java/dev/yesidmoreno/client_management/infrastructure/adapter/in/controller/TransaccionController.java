package dev.yesidmoreno.client_management.infrastructure.adapter.in.controller;

import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.in.TransaccionUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransaccionController {

    private final TransaccionUseCase transaccionUseCase;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Transaccion> doTransaccion(@RequestParam UUID originId, @RequestParam(required = false) UUID targetId, @RequestParam Transaccion.TransactionType type, @RequestParam Double amount) {
        return new ResponseEntity<>(transaccionUseCase.doTransaccion(originId, targetId, type, amount), HttpStatus.OK);
    }
}
