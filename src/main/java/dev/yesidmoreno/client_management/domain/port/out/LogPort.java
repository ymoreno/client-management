package dev.yesidmoreno.client_management.domain.port.out;

public interface LogPort {

    void info(String message);

    void warn(String message);

    void error(String messaget);

}
