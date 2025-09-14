package dev.yesidmoreno.client_management.domain.port.in;

import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.model.Transaccion;

import java.util.List;

public interface TransaccionUseCase {
    Transaccion doTransaccion(List<Producto> cuentas, Transaccion.TransactionType type);
}