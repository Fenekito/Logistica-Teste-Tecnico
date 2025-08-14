// src/main/java/com/krill/logistica/model/TipoPaletizacao.java
package com.krill.logistica.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_paletizacao", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Paletizacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String nome; // "PALETIZADO" | "NAO_PALETIZADO"

    // Usado no cálculo de ocupação (Não paletizado = dobra pallets)
    @Column(nullable = false)
    private Integer multiplicadorFaixa; // 1 (paletizado) ou 2 (não paletizado)
}
