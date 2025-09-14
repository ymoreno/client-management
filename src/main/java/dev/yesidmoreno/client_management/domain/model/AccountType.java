package dev.yesidmoreno.client_management.domain.model;

import lombok.Getter;

@Getter
public enum AccountType {
    CC("CC", "33", "Cuenta Corriente"),
    CA("CA", "53", "Cuenta de Ahorros");

    private final String code;
    private final String prefix;
    private final String displayName;

    AccountType(String code, String prefix, String displayName) {
        this.code = code;
        this.prefix = prefix;
        this.displayName = displayName;
    }
}
