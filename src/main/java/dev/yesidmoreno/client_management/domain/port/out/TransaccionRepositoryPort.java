package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.Producto;
import dev.yesidmoreno.client_management.domain.model.Transaccion;

import java.util.UUID;

public interface TransaccionRepositoryPort {

    Transaccion save(Transaccion transaccion);
}