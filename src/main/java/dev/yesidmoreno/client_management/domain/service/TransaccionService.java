package dev.yesidmoreno.client_management.domain.service;

import dev.yesidmoreno.client_management.Exception.NotFoundException;
import dev.yesidmoreno.client_management.Exception.ValidationException;
import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.in.TransaccionUseCase;
import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import dev.yesidmoreno.client_management.domain.port.out.ProductoRepositoryPort;
import dev.yesidmoreno.client_management.domain.port.out.TransaccionRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransaccionService implements TransaccionUseCase {

    private final TransaccionRepositoryPort transaccionRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;
    private final LogPort logPort;

    @Override
    @Transactional
    public Transaccion doTransaccion(UUID originId, UUID targetId, Transaccion.TransactionType type, Double amount) {
        logPort.info("Iniciando transacción: " + type + " desde " + originId + " hacia " + targetId + " por un monto de " + amount);

        Producto originAccount = null;
        Producto targetAccount = null;

        switch (type) {
            case DEPOSIT -> {
                logPort.info("Procesando depósito en la cuenta: " + originId);
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> {
                            logPort.error("Error en depósito: Cuenta no encontrada con id: " + originId);
                            return new NotFoundException("Cuenta no encontrada");
                        });
                originAccount.setBalance(originAccount.getBalance() + amount);
                productoRepositoryPort.save(originAccount);
                logPort.info("Depósito exitoso en la cuenta: " + originId);
            }
            case WITHDRAWAL -> {
                logPort.info("Procesando retiro de la cuenta: " + originId);
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> {
                            logPort.error("Error en retiro: Cuenta no encontrada con id: " + originId);
                            return new NotFoundException("Cuenta no encontrada");
                        });
                if (originAccount.getBalance() < amount) {
                    logPort.error("Error en retiro: Saldo insuficiente en la cuenta: " + originId);
                    throw new ValidationException("Saldo insuficiente");
                }
                originAccount.setBalance(originAccount.getBalance() - amount);
                productoRepositoryPort.save(originAccount);
                logPort.info("Retiro exitoso de la cuenta: " + originId);
            }
            case TRANSFER -> {
                logPort.info("Procesando transferencia desde la cuenta: " + originId + " hacia la cuenta: " + targetId);
                if (originId.equals(targetId)) {
                    logPort.error("Error en transferencia: Las cuentas de Origen y Destino deben ser diferentes");
                    throw new ValidationException("Las cuentas de Origen y Destino deben ser diferentes");
                }
                originAccount = productoRepositoryPort.getProductoById(originId)
                        .orElseThrow(() -> {
                            logPort.error("Error en transferencia: Cuenta de origen no encontrada con id: " + originId);
                            return new NotFoundException("Cuenta de origen no encontrada");
                        });
                targetAccount = productoRepositoryPort.getProductoById(targetId)
                        .orElseThrow(() -> {
                            logPort.error("Error en transferencia: Cuenta destino no encontrada con id: " + targetId);
                            return new NotFoundException("Cuenta destino no encontrada");
                        });
                if (originAccount.getBalance() < amount) {
                    logPort.error("Error en transferencia: Saldo insuficiente en cuenta origen: " + originId);
                    throw new ValidationException("Saldo insuficiente en cuenta origen");
                }
                originAccount.setBalance(originAccount.getBalance() - amount);
                targetAccount.setBalance(targetAccount.getBalance() + amount);
                productoRepositoryPort.save(originAccount);
                productoRepositoryPort.save(targetAccount);
                logPort.info("Transferencia exitosa desde la cuenta: " + originId + " hacia la cuenta: " + targetId);
            }
            default -> {
                logPort.error("Error en transacción: Tipo de transacción no soportado: " + type);
                throw new ValidationException("Tipo de transacción no soportado");
            }
        }
        Transaccion tx = new Transaccion();
        tx.setTransactionType(type);
        tx.setAmount(amount);
        tx.setOrigin(originAccount);
        tx.setTarget(targetAccount);
        tx.setDateTime(LocalDateTime.now());
        Transaccion createdTx = transaccionRepositoryPort.save(tx);
        logPort.info("Transacción registrada exitosamente con id: " + createdTx.getId());
        return createdTx;
    }
}
