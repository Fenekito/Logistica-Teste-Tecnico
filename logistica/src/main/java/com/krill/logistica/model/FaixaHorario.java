// src/main/java/com/krill/logistica/model/FaixaHorario.java
package com.krill.logistica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(
    name = "faixa_horario",
    uniqueConstraints = @UniqueConstraint(columnNames = {"inicio", "fim"})
)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FaixaHorario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime inicio;

    @Column(nullable = false)
    private LocalTime fim;

    // Regra do PDF: 120 por faixa (não bloqueia, só notifica no agendamento)
    @Column(nullable = false)
    private Integer capacidadeMaxima; // default 120

    @Column(length = 50)
    private String descricao;
}
