package com.krill.logistica.dto;

import com.krill.logistica.model.Role;

import jakarta.validation.constraints.*;

public record UpdateUserDto(
  @Size(max=80) String nome,
  @Email String email,
  @Size(min=6) String senha,
  Role role
) {}
