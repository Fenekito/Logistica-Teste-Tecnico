package com.krill.logistica.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateAgendamentoDto(
        @NotNull LocalDate data,
        @NotNull Long faixaId,
        @NotNull Long idPedido,
        @NotBlank @Size(max = 80) String fornecedor,
        @NotBlank @Email @Size(max = 80) String emailFornecedor,
        @NotNull Long tipoCaminhaoId,
        @NotNull Long tipoPaletizacaoId,
        @Size(max = 500) String observacao
) { }
