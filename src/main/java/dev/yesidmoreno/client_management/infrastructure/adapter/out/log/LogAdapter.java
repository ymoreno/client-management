package dev.yesidmoreno.client_management.infrastructure.adapter.out.log;

import dev.yesidmoreno.client_management.domain.port.out.LogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogAdapter implements LogPort {

    private static final Logger logger = LoggerFactory.getLogger(LogAdapter.class);

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }
}