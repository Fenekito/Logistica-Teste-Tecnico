package com.krill.logistica.dto;

import com.krill.logistica.model.Role;

import jakarta.validation.constraints.*;

public record CreateUserDto(
  @NotBlank @Size(max=80) String nome,
  @Email String email,
  @NotBlank @Size(min=6) String senha,
  @NotNull Role role
) {}
