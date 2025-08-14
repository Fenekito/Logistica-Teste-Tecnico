// src/main/java/com/krill/logistica/controller/TipoCaminhaoController.java
package com.krill.logistica.controller;

import com.krill.logistica.dto.CaminhaoDto;
import com.krill.logistica.model.Caminhao;
import com.krill.logistica.service.CaminhaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-caminhao")
@RequiredArgsConstructor
public class CaminhaoController {
    private final CaminhaoService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Caminhao create(@RequestBody @Valid CaminhaoDto dto) { return service.create(dto); }

    @GetMapping public List<Caminhao> list() { return service.list(); }

    @PutMapping("/{id}") public Caminhao update(@PathVariable Long id, @RequestBody @Valid CaminhaoDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
