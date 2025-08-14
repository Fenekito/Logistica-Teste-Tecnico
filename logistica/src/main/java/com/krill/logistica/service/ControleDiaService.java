// src/main/java/com/krill/logistica/service/ControleDiaService.java
package com.krill.logistica.service;

import com.krill.logistica.dto.ControleDiaDto;
import com.krill.logistica.model.ControleDia;
import com.krill.logistica.repository.ControleDiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ControleDiaService {
    private final ControleDiaRepository repo;

    public ControleDia create(ControleDiaDto dto) {
        if (repo.existsByData(dto.data())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data já cadastrada");
        }
        return repo.save(ControleDia.builder()
                .data(dto.data())
                .capacidadeMaxima(dto.capacidadeMaxima() == null ? 480 : dto.capacidadeMaxima())
                .build());
    }

    public List<ControleDia> list() { return repo.findAll(); }

    public ControleDia update(Long id, ControleDiaDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        entity.setData(dto.data());
        entity.setCapacidadeMaxima(dto.capacidadeMaxima() == null ? 480 : dto.capacidadeMaxima());
        return repo.save(entity);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
