package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.domain.model.Transaccion;
import dev.yesidmoreno.client_management.domain.port.out.TransaccionRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransaccionRepositoryAdapter implements TransaccionRepositoryPort {

    TransaccionJpaRepository transaccionJpaRepository;

    @Override
    public Transaccion save(Transaccion transaccion) {
        return transaccionJpaRepository.save(transaccion);
    }
}
