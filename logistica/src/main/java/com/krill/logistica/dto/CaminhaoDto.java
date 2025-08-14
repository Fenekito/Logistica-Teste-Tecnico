// src/main/java/com/krill/logistica/dto/TipoCaminhaoDto.java
package com.krill.logistica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CaminhaoDto(
        @NotBlank @Size(max = 30) String nome
) {}
