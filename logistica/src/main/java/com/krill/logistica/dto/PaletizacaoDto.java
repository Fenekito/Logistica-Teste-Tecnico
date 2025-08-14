// src/main/java/com/krill/logistica/dto/TipoPaletizacaoDto.java
package com.krill.logistica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaletizacaoDto (
        @NotBlank @Size(max = 30) String nome // "PALETIZADO" ou "NAO_PALETIZADO"
) {}
