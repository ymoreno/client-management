package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.in.TransaccionUseCase;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.TransaccionRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransaccionService implements TransaccionUseCase {

    private TransaccionRepositoryPort transaccionRepositoryPort;
    private ProductoRepositoryPort productoRepositoryPort;


    @Override
    @Transactional
    public Transaccion doTransaccion(UUID originId, UUID targetId, Transaccion.TransactionType type, Double amount) {

        Producto originAccount = null;
        Producto targetAccount = null;

        switch (type) {
            case DEPOSIT -> {
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
                originAccount.setBalance(originAccount.getBalance() + amount);
                productoRepositoryPort.save(originAccount);
            }
            case WITHDRAWAL -> {
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
                if (originAccount.getBalance() < amount) {
                    throw new ValidationException("Saldo insuficiente");
                }
                originAccount.setBalance(originAccount.getBalance() - amount);
                productoRepositoryPort.save(originAccount);
            }
            case TRANSFER -> {
                if (originId.equals(targetId)) {
                    throw new ValidationException("Las cuentas de Origen y Destino deben ser diferentes");
                }
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> new NotFoundException("Cuenta de origen no encontrada"));
                targetAccount = productoRepositoryPort.getProductoById(targetId)
                        .orElseThrow(() -> new NotFoundException("Cuenta destino no encontrada"));
                if (originAccount.getBalance() < amount) {
                    throw new ValidationException("Saldo insuficiente en cuenta origen");
                }
                originAccount.setBalance(originAccount.getBalance() - amount);
                targetAccount.setBalance(targetAccount.getBalance() + amount);
                productoRepositoryPort.save(originAccount);
                productoRepositoryPort.save(targetAccount);
            }
            default -> throw new ValidationException("Tipo de transacción no soportado");
        }
            Transaccion tx = new Transaccion();
            tx.setTransactionType(type);
            tx.setAmount(amount);
            tx.setOrigin(originAccount);
            tx.setTarget(targetAccount);
            tx.setDateTime(LocalDateTime.now());
            return transaccionRepositoryPort.save(tx);
    }
}