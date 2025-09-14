package dev.yesidmoreno.client_management.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Size(min = 10, max = 10, message = "El número de cuenta debe tener exactamente 10 dígitos")
    @Pattern(regexp = "\\d+", message = "El número de cuenta solo puede contener dígitos")
    private String accountNumber;
    @NotNull
    @PositiveOrZero(message = "El saldo no puede ser negativo")
    private Double balance;
    private boolean gmfExempt;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime modificationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente owner;

    @AssertTrue(message = "El número de cuenta no cumple con el prefijo del tipo seleccionado")
    public boolean isPrefijoValido() {
        if (accountNumber == null || accountType == null) return true;
        return accountNumber.startsWith(accountType.getPrefix());
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        CANCELLED
    }

}