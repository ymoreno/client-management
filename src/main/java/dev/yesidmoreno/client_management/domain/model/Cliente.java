package dev.yesidmoreno.client_management.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private long numberId;
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String names;
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String surname;
    @Email(message = "Correo inválido, debe ser del formato xxxx@xxxxx.xxx")
    private String email;
    private LocalDate birthDate;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime modificationDate;

    public enum DocumentType {
        CC,
        CE,
        NIT
    }
}