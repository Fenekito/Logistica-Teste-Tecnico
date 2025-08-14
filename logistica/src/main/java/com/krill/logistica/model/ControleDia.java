// src/main/java/com/krill/logistica/model/ControleDia.java
package com.krill.logistica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "controle_dia", uniqueConstraints = @UniqueConstraint(columnNames = "data"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ControleDia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    // Regra do PDF: 480 por dia (não bloqueia, só notifica no agendamento)
    @Column(nullable = false)
    private Integer capacidadeMaxima; // default 480
}
