package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.Transaccion;

public interface TransaccionRepositoryPort {

    Transaccion save(Transaccion transaccion);
}