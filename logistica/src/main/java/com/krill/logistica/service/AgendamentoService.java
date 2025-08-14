package com.krill.logistica.service;

import com.krill.logistica.dto.AgendamentoDto;
import com.krill.logistica.dto.CreateAgendamentoDto;
import com.krill.logistica.model.*;
import com.krill.logistica.model.Agendamento.StatusAgendamento;
import com.krill.logistica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repo;
    private final FaixaHorarioRepository faixaRepo;
    private final CaminhaoRepository caminhaoRepo;
    private final PaletizacaoRepository paletizacaoRepo;
    private final ControleDiaRepository controleDiaRepo;

    @Transactional
    public AgendamentoDto create(CreateAgendamentoDto dto) {
        FaixaHorario faixa = faixaRepo.findById(dto.faixaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faixa inválida"));
        Caminhao cam = caminhaoRepo.findById(dto.tipoCaminhaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de caminhão inválido"));
        Paletizacao pal = paletizacaoRepo.findById(dto.tipoPaletizacaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de paletização inválido"));

        // Persiste
        Agendamento ag = repo.save(Agendamento.builder()
                .data(dto.data())
                .faixa(faixa)
                .idPedido(dto.idPedido())
                .fornecedor(dto.fornecedor().trim())
                .emailFornecedor(dto.emailFornecedor().trim())
                .tipoCaminhao(cam)
                .tipoPaletizacao(pal)
                .observacao(dto.observacao())
                .status(StatusAgendamento.ATIVO)
                .build());

        // Cálculo de ocupações/avisos (após inserir)
        var info = calcularOcupacoes(dto.data(), faixa);

        String mailto = buildMailto(ag, info);

        return toDto(ag, info, mailto);
    }

    public List<AgendamentoDto> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Agendamento> list = repo.findByDataBetweenOrderByCriadoEmDesc(inicio, fim);
        return list.stream().map(a -> {
            var info = calcularOcupacoes(a.getData(), a.getFaixa());
            return toDto(a, info, null);
        }).toList();
    }

    public List<AgendamentoDto> listarDoDia(LocalDate data) {
        return repo.findByDataOrderByCriadoEmAsc(data).stream()
                .map(a -> toDto(a, calcularOcupacoes(a.getData(), a.getFaixa()), null))
                .toList();
    }

    @Transactional
    public void cancelar(Long id) {
        var ag = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        ag.setStatus(StatusAgendamento.CANCELADO);
        repo.save(ag);
    }

    // ======= Helpers =======

    private record OcupacaoInfo(
            long totalDiaUnidades,
            int capacidadeDia,
            double pctDia,
            boolean excedeuDia,
            long totalFaixaUnidades,
            int capacidadeFaixa,
            double pctFaixa,
            boolean excedeuFaixa
    ) {}

    private OcupacaoInfo calcularOcupacoes(LocalDate data, FaixaHorario faixa) {
        // Capacidade padrão do PDF
        int capDia = 480;
        var controleDia = controleDiaRepo.findAll().stream()
                .filter(c -> c.getData().equals(data)).findFirst().orElse(null);
        if (controleDia != null) {
            capDia = controleDia.getCapacidadeMaxima();
        }

        int capFaixa = faixa.getCapacidadeMaxima() == null ? 120 : faixa.getCapacidadeMaxima();

        long diaP = repo.countDiaPaletizado(data, StatusAgendamento.ATIVO);
        long diaNP = repo.countDiaNaoPaletizado(data, StatusAgendamento.ATIVO);
        long totalDia = diaP + (2L * diaNP);

        long fxP = repo.countFaixaPaletizado(data, faixa, StatusAgendamento.ATIVO);
        long fxNP = repo.countFaixaNaoPaletizado(data, faixa, StatusAgendamento.ATIVO);
        long totalFaixa = fxP + (2L * fxNP);

        double pctDia = capDia == 0 ? 0 : (totalDia * 100.0 / capDia);
        double pctFaixa = capFaixa == 0 ? 0 : (totalFaixa * 100.0 / capFaixa);

        return new OcupacaoInfo(
                totalDia, capDia, round2(pctDia), totalDia > capDia,
                totalFaixa, capFaixa, round2(pctFaixa), totalFaixa > capFaixa
        );
    }

    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    private AgendamentoDto toDto(Agendamento a, OcupacaoInfo i, String mailto) {
        return new AgendamentoDto(
                a.getId(),
                a.getData(),
                a.getFaixa().getId(),
                a.getFaixa().getDescricao(),
                a.getIdPedido(),
                a.getFornecedor(),
                a.getEmailFornecedor(),
                a.getTipoCaminhao().getId(),
                a.getTipoCaminhao().getNome(),
                a.getTipoPaletizacao().getId(),
                a.getTipoPaletizacao().getNome(),
                a.getObservacao(),
                a.getStatus(),
                i.pctDia, i.pctFaixa, i.excedeuDia, i.excedeuFaixa,
                mailto,
                a.getCriadoEm()
        );
    }

    private String buildMailto(Agendamento a, OcupacaoInfo i) {
    	String paletizacaoFormatada = a.getTipoPaletizacao().getNome() == "Paletizado" ? a.getTipoPaletizacao().getNome() : "Não Paletizado";
        String subject = "[Agendamento] Pedido " + a.getIdPedido() + " - " + a.getData();
        String body = """
                Olá %s,

                Seu agendamento foi registrado.

                Dados:
                • Data: %s
                • Faixa: %s
                • Pedido: %d
                • Caminhão: %s
                • Paletização: %s
                • Observação: %s

                Ocupação estimada:
                • Dia: %.2f%% (cap. %d)
                • Faixa: %.2f%% (cap. %d)

                Atenciosamente,
                Logística
                """.formatted(
                a.getFornecedor(),
                a.getData(),
                a.getFaixa().getDescricao(),
                a.getIdPedido(),
                a.getTipoCaminhao().getNome(),
                paletizacaoFormatada,
                a.getObservacao() == null ? "-" : a.getObservacao(),
                i.pctDia, i.capacidadeDia,
                i.pctFaixa, i.capacidadeFaixa
        );

        return "mailto:" + url(a.getEmailFornecedor())
                + "?subject=" + url(subject)
                + "&body=" + url(body);
    }

    private static String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
