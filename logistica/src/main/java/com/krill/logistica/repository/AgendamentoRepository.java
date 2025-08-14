package com.krill.logistica.repository;

import com.krill.logistica.model.Agendamento;
import com.krill.logistica.model.FaixaHorario;
import com.krill.logistica.model.Agendamento.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByDataBetweenOrderByDataAsc(LocalDate inicio, LocalDate fim);

    List<Agendamento> findByDataOrderByCriadoEmAsc(LocalDate data);

    @Query("""
        select count(a) from Agendamento a
         where a.data = :data and a.status = :status
         and a.tipoPaletizacao.multiplicadorFaixa = 1
    """)
    long countDiaPaletizado(LocalDate data, StatusAgendamento status);

    @Query("""
        select count(a) from Agendamento a
         where a.data = :data and a.status = :status
         and a.tipoPaletizacao.multiplicadorFaixa = 2
    """)
    long countDiaNaoPaletizado(LocalDate data, StatusAgendamento status);

    @Query("""
        select count(a) from Agendamento a
         where a.data = :data and a.faixa = :faixa and a.status = :status
         and a.tipoPaletizacao.multiplicadorFaixa = 1
    """)
    long countFaixaPaletizado(LocalDate data, FaixaHorario faixa, StatusAgendamento status);

    @Query("""
        select count(a) from Agendamento a
         where a.data = :data and a.faixa = :faixa and a.status = :status
         and a.tipoPaletizacao.multiplicadorFaixa = 2
    """)
    long countFaixaNaoPaletizado(LocalDate data, FaixaHorario faixa, StatusAgendamento status);

    List<Agendamento> findByDataBetweenOrderByCriadoEmDesc(LocalDate inicio, LocalDate fim);
}
