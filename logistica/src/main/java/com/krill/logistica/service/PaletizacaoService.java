// src/main/java/com/krill/logistica/service/TipoPaletizacaoService.java
package com.krill.logistica.service;

import com.krill.logistica.dto.PaletizacaoDto;
import com.krill.logistica.model.Paletizacao;
import com.krill.logistica.repository.PaletizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaletizacaoService {
    private final PaletizacaoRepository repo;

    public Paletizacao create(PaletizacaoDto dto) {
        String nome = dto.nome().trim().toUpperCase();
        if (!nome.equals("PALETIZADO") && !nome.equals("NAO_PALETIZADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valores permitidos: PALETIZADO, NAO_PALETIZADO");
        }
        if (repo.existsByNomeIgnoreCase(nome)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de paletização já existe");
        }
        int mult = nome.equals("NAO_PALETIZADO") ? 2 : 1;
        return repo.save(Paletizacao.builder().nome(nome).multiplicadorFaixa(mult).build());
    }

    public List<Paletizacao> list() { return repo.findAll(); }

    public Paletizacao update(Long id, PaletizacaoDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        String nome = dto.nome().trim().toUpperCase();
        if (!nome.equals("PALETIZADO") && !nome.equals("NAO_PALETIZADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valores permitidos: PALETIZADO, NAO_PALETIZADO");
        }
        entity.setNome(nome);
        entity.setMultiplicadorFaixa(nome.equals("NAO_PALETIZADO") ? 2 : 1);
        return repo.save(entity);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
