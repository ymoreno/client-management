package dev.yesidmoreno.client_management.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private TransactionType transactionType;

    @NotNull
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "origin_id", nullable = false)
    private Producto origin;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Producto target;

    @NotNull
    private java.time.LocalDateTime dateTime;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }
}