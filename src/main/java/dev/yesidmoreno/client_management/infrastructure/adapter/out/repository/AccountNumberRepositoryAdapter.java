package dev.yesidmoreno.client_management.infrastructure.adapter.out.repository;

import dev.yesidmoreno.client_management.domain.model.AccountType;
import dev.yesidmoreno.client_management.domain.port.out.AccountNumberRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountNumberRepositoryAdapter implements AccountNumberRepositoryPort {

    private final JdbcTemplate jdbc;

    @Override
    public String generateAccountNumber(AccountType type) {
        String seqName = (type == AccountType.CA) ? "seq_account_53" : "seq_account_33";
        Long nextVal = jdbc.queryForObject("SELECT nextval('" + seqName + "')", Long.class);
        return type.getPrefix() + String.format("%08d", nextVal);
    }
}
