// src/main/java/com/krill/logistica/dto/FaixaHorarioDto.java
package com.krill.logistica.dto;

import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record FaixaHorarioDto(
        @NotNull LocalTime inicio,
        @NotNull LocalTime fim,
        @Positive @Max(100000) Integer capacidadeMaxima, // default 120 se null
        @Size(max = 50) String descricao
) {}
