package com.krill.logistica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "agendamento", indexes = {
        @Index(name = "idx_ag_data", columnList = "data"),
        @Index(name = "idx_ag_status", columnList = "status")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Agendamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faixa_id", nullable = false)
    private FaixaHorario faixa;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(nullable = false, length = 80)
    private String fornecedor;

    @Column(nullable = false, length = 80)
    private String emailFornecedor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_caminhao_id", nullable = false)
    private Caminhao tipoCaminhao;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_paletizacao_id", nullable = false)
    private Paletizacao tipoPaletizacao;

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusAgendamento status;

    @Column(nullable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (status == null) status = StatusAgendamento.ATIVO;
        if (criadoEm == null) criadoEm = OffsetDateTime.now();
    }

    public enum StatusAgendamento { ATIVO, CANCELADO }
}
