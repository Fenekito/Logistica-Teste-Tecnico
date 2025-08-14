// src/main/java/com/krill/logistica/repository/TipoPaletizacaoRepository.java
package com.krill.logistica.repository;

import com.krill.logistica.model.Paletizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaletizacaoRepository extends JpaRepository<Paletizacao, Long> {
    Optional<Paletizacao> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
