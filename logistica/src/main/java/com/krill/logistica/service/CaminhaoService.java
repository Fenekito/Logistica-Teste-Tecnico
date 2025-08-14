// src/main/java/com/krill/logistica/service/TipoCaminhaoService.java
package com.krill.logistica.service;

import com.krill.logistica.dto.CaminhaoDto;
import com.krill.logistica.model.Caminhao;
import com.krill.logistica.repository.CaminhaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaminhaoService {
    private final CaminhaoRepository repo;

    public Caminhao create(CaminhaoDto dto) {
        if (repo.existsByNomeIgnoreCase(dto.nome())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de caminhão já existe");
        }
        return repo.save(Caminhao.builder().nome(dto.nome().trim()).build());
    }

    public List<Caminhao> list() { return repo.findAll(); }

    public Caminhao update(Long id, CaminhaoDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        entity.setNome(dto.nome().trim());
        return repo.save(entity);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
