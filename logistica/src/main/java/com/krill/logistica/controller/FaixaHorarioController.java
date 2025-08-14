// src/main/java/com/krill/logistica/controller/FaixaHorarioController.java
package com.krill.logistica.controller;

import com.krill.logistica.dto.FaixaHorarioDto;
import com.krill.logistica.model.FaixaHorario;
import com.krill.logistica.service.FaixaHorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faixas-horario")
@RequiredArgsConstructor
public class FaixaHorarioController {
    private final FaixaHorarioService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public FaixaHorario create(@RequestBody @Valid FaixaHorarioDto dto) { return service.create(dto); }

    @GetMapping public List<FaixaHorario> list() { return service.list(); }

    @PutMapping("/{id}") public FaixaHorario update(@PathVariable Long id, @RequestBody @Valid FaixaHorarioDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
