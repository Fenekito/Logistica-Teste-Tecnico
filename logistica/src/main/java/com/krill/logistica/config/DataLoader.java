package com.krill.logistica.config;

import com.krill.logistica.model.FaixaHorario;
import com.krill.logistica.model.Caminhao;
import com.krill.logistica.model.Paletizacao;
import com.krill.logistica.repository.FaixaHorarioRepository;
import com.krill.logistica.repository.CaminhaoRepository;
import com.krill.logistica.repository.PaletizacaoRepository;
import com.krill.logistica.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CaminhaoRepository caminhaoRepo;
    private final PaletizacaoRepository paletizacaoRepo;
    private final FaixaHorarioRepository faixaRepo;
    private final UserService userService;

    @Override
    public void run(String... args) {
        seedCaminhoes();
        seedPaletizacoes();
        seedUsuarios();
    }

    private void seedUsuarios() {
        userService.saveAdminIfAbsent("Administrador", "admin@krill.local", "admin123");
        userService.saveUserIfAbsent("Usuário", "user@krill.local", "user123");
    }

    private void seedCaminhoes() {
        List<String> nomes = List.of("truck", "carreta", "van");
        nomes.forEach(n -> caminhaoRepo.findByNomeIgnoreCase(n)
                .orElseGet(() -> caminhaoRepo.save(Caminhao.builder().nome(n).build())));
    }

    private void seedPaletizacoes() {
        var paletizado = "PALETIZADO";
        var nao = "NAO_PALETIZADO";
        paletizacaoRepo.findByNomeIgnoreCase(paletizado)
                .orElseGet(() -> paletizacaoRepo.save(Paletizacao.builder()
                        .nome(paletizado).multiplicadorFaixa(1).build()));
        paletizacaoRepo.findByNomeIgnoreCase(nao)
                .orElseGet(() -> paletizacaoRepo.save(Paletizacao.builder()
                        .nome(nao).multiplicadorFaixa(2).build()));
    }

}
