package com.krill.logistica.controller;

import com.krill.logistica.dto.AgendamentoDto;
import com.krill.logistica.dto.CreateAgendamentoDto;
import com.krill.logistica.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    // ADMIN pode criar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AgendamentoDto create(@RequestBody @Valid CreateAgendamentoDto dto) {
        return service.create(dto);
    }

    // ADMIN e USER podem consultar por período (relatório)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<AgendamentoDto> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.listarPorPeriodo(inicio, fim);
    }

    // ADMIN e USER podem ver do dia (apoio para dashboard)
    @GetMapping("/dia/{data}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<AgendamentoDto> listarDoDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return service.listarDoDia(data);
    }

    // ADMIN pode cancelar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelar(@PathVariable Long id) {
        service.cancelar(id);
    }
}
