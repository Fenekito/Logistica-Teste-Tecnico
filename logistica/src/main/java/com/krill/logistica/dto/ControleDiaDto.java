// src/main/java/com/krill/logistica/dto/ControleDiaDto.java
package com.krill.logistica.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ControleDiaDto(
        @NotNull LocalDate data,
        @Positive @Max(100000) Integer capacidadeMaxima // default 480 se null
) {}
