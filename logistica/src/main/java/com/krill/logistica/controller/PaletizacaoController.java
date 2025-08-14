// src/main/java/com/krill/logistica/controller/TipoPaletizacaoController.java
package com.krill.logistica.controller;

import com.krill.logistica.dto.PaletizacaoDto;
import com.krill.logistica.model.Paletizacao;
import com.krill.logistica.service.PaletizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-paletizacao")
@RequiredArgsConstructor
public class PaletizacaoController {
    private final PaletizacaoService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Paletizacao create(@RequestBody @Valid PaletizacaoDto dto) { return service.create(dto); }

    @GetMapping public List<Paletizacao> list() { return service.list(); }

    @PutMapping("/{id}") public Paletizacao update(@PathVariable Long id, @RequestBody @Valid PaletizacaoDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
