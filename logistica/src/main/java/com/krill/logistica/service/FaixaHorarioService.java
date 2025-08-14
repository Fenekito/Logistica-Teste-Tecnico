// src/main/java/com/krill/logistica/service/FaixaHorarioService.java
package com.krill.logistica.service;

import com.krill.logistica.dto.FaixaHorarioDto;
import com.krill.logistica.model.FaixaHorario;
import com.krill.logistica.repository.FaixaHorarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaixaHorarioService {
    private final FaixaHorarioRepository repo;

    public FaixaHorario create(FaixaHorarioDto dto) {
        validar2Horas(dto);
        if (repo.existsByInicioAndFim(dto.inicio(), dto.fim())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Faixa já cadastrada");
        }
        return repo.save(FaixaHorario.builder()
                .inicio(dto.inicio())
                .fim(dto.fim())
                .capacidadeMaxima(dto.capacidadeMaxima() == null ? 120 : dto.capacidadeMaxima())
                .descricao(dto.descricao())
                .build());
    }

    public List<FaixaHorario> list() { return repo.findAll(); }

    public FaixaHorario update(Long id, FaixaHorarioDto dto) {
        validar2Horas(dto);
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        entity.setInicio(dto.inicio());
        entity.setFim(dto.fim());
        entity.setCapacidadeMaxima(dto.capacidadeMaxima() == null ? 120 : dto.capacidadeMaxima());
        entity.setDescricao(dto.descricao());
        return repo.save(entity);
    }

    public void delete(Long id) { repo.deleteById(id); }

    private void validar2Horas(FaixaHorarioDto dto) {
        var dur = Duration.between(dto.inicio(), dto.fim());
        if (!dur.equals(Duration.ofHours(2))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faixa deve ter exatamente 2 horas");
        }
    }
}
