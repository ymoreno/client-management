package dev.yesidmoreno.client_management.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private String names;
    private String surname;
    private String email;
    private LocalDate birthDate;

    public enum DocumentType {
        CC, // Cédula de Ciudadanía
        CE, // Cédula de Extranjería
        TI, // Tarjeta de Identidad
        NIT, // Número de Identificación Tributaria
        PASSPORT // Pasaporte
    }
}