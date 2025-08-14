// src/main/java/com/krill/logistica/repository/TipoCaminhaoRepository.java
package com.krill.logistica.repository;

import com.krill.logistica.model.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    Optional<Caminhao> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
