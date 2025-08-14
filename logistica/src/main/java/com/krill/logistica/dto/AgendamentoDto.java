package com.krill.logistica.dto;

import com.krill.logistica.model.Agendamento.StatusAgendamento;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AgendamentoDto(
        Long id,
        LocalDate data,
        Long faixaId,
        String faixaDescricao,
        Long idPedido,
        String fornecedor,
        String emailFornecedor,
        Long tipoCaminhaoId,
        String tipoCaminhao,
        Long tipoPaletizacaoId,
        String tipoPaletizacao,
        String observacao,
        StatusAgendamento status,
        // infos auxiliares
        double ocupacaoDiaPct,
        double ocupacaoFaixaPct,
        boolean excedeuDia,
        boolean excedeuFaixa,
        String mailto,
        OffsetDateTime criadoEm
) { }
