// src/main/java/com/krill/logistica/model/TipoCaminhao.java
package com.krill.logistica.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_caminhao", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Caminhao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String nome; // "truck", "carreta", "van"
}
