package com.krill.logistica.dto;

import jakarta.validation.constraints.*;

public record LoginRequestDto(
    @NotBlank @Email String email,
    @NotBlank String password
) {}
