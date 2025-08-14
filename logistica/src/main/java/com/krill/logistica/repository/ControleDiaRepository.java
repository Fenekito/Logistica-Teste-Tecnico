// src/main/java/com/krill/logistica/repository/ControleDiaRepository.java
package com.krill.logistica.repository;

import com.krill.logistica.model.ControleDia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ControleDiaRepository extends JpaRepository<ControleDia, Long> {
    boolean existsByData(LocalDate data);
}
