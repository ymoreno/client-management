package dev.yesidmoreno.client_management.infrastructure.config;

import dev.yesidmoreno.client_management.domain.port.out.*;
import dev.yesidmoreno.client_management.domain.service.ClienteService;
import dev.yesidmoreno.client_management.domain.service.ProductoService;
import dev.yesidmoreno.client_management.domain.service.TransaccionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ClienteService clienteService(ClienteRepositoryPort clienteRepository, ProductoRepositoryPort productoRepositoryPort, LogPort logPort) {
        return new ClienteService(clienteRepository, productoRepositoryPort, logPort);
    }

    @Bean
    public ProductoService productoService(ProductoRepositoryPort productoRepository,
                                           AccountNumberRepositoryPort accountNumberRepository,
                                           LogPort logPort) {
        return new ProductoService(productoRepository, accountNumberRepository, logPort);
    }

    @Bean
    public TransaccionService transaccionService(ProductoRepositoryPort productoRepository,
                                                 TransaccionRepositoryPort transaccionRepository,
                                                 LogPort logPort) {
        return new TransaccionService(transaccionRepository, productoRepository, logPort);
    }
}
