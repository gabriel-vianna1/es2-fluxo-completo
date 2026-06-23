package com.agenda;

import com.agenda.controller.AtendimentoController;
import com.agenda.model.Atendimento;
import com.agenda.model.ProfissionalSaude;
import com.agenda.model.Categoria;
import com.agenda.model.TipoReceita;
import com.agenda.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - Atendimento
 */
@WebMvcTest(AtendimentoController.class)
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendimentoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarAtendimentoComSucesso() throws Exception {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setId(1L);
        prof.setNome("Dra. Ana Lima");
        prof.setCategoria(Categoria.MEDICO);

        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setTitulo("Consulta de rotina");
        atendimento.setData(LocalDate.of(2026, 7, 10));
        atendimento.setHorario(LocalTime.of(14, 30));
        atendimento.setLinkVideoConferencia("https://meet.google.com/abc-def");
        atendimento.setTipoReceita(TipoReceita.REMEDIO);
        atendimento.setProfissional(prof);

        objectMapper.registerModule(new JavaTimeModule());
        when(repository.save(any(Atendimento.class))).thenReturn(atendimento);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Consulta de rotina"))
                .andExpect(jsonPath("$.tipoReceita").value("REMEDIO"));
    }

    @Test
    void deveListarAtendimentosVazio() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaAtendimentoInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirAtendimentoComSucesso() throws Exception {
        when(repository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/atendimentos/1"))
                .andExpect(status().isNoContent());
    }
}
