package dev.yesidmoreno.client_management.domain.port.out;

import dev.yesidmoreno.client_management.domain.model.AccountType;

public interface AccountNumberRepositoryPort {

    String generateAccountNumber(AccountType type);
}
