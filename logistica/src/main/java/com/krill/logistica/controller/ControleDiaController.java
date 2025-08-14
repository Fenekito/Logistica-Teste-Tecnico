// src/main/java/com/krill/logistica/controller/ControleDiaController.java
package com.krill.logistica.controller;

import com.krill.logistica.dto.ControleDiaDto;
import com.krill.logistica.model.ControleDia;
import com.krill.logistica.service.ControleDiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/controle-dia")
@RequiredArgsConstructor
public class ControleDiaController {
    private final ControleDiaService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ControleDia create(@RequestBody @Valid ControleDiaDto dto) { return service.create(dto); }

    @GetMapping public List<ControleDia> list() { return service.list(); }

    @PutMapping("/{id}") public ControleDia update(@PathVariable Long id, @RequestBody @Valid ControleDiaDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
