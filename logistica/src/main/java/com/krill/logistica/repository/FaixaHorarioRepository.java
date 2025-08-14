// src/main/java/com/krill/logistica/repository/FaixaHorarioRepository.java
package com.krill.logistica.repository;

import com.krill.logistica.model.FaixaHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;

public interface FaixaHorarioRepository extends JpaRepository<FaixaHorario, Long> {
    boolean existsByInicioAndFim(LocalTime inicio, LocalTime fim);
}
