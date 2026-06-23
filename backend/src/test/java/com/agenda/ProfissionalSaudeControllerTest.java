package com.agenda;

import com.agenda.controller.ProfissionalSaudeController;
import com.agenda.model.ProfissionalSaude;
import com.agenda.model.Categoria;
import com.agenda.repository.ProfissionalSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - ProfissionalSaude
 * Usa @WebMvcTest para testar apenas o controller isoladamente.
 * O repository é mockado com @MockBean.
 */
@WebMvcTest(ProfissionalSaudeController.class)
class ProfissionalSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalSaudeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProfissionalComSucesso() throws Exception {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setId(1L);
        prof.setNome("Dra. Ana Lima");
        prof.setTelefone("31988887777");
        prof.setEndereco("Rua das Flores, 100");
        prof.setCategoria(Categoria.MEDICO);

        when(repository.save(any(ProfissionalSaude.class))).thenReturn(prof);

        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prof)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dra. Ana Lima"))
                .andExpect(jsonPath("$.categoria").value("MEDICO"));
    }

    @Test
    void deveListarProfissionaisVazio() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaProfissionalInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionais/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarPorCategoria() throws Exception {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setId(1L);
        prof.setNome("Dr. Bruno Costa");
        prof.setCategoria(Categoria.PSICOLOGO);

        when(repository.findByCategoria(Categoria.PSICOLOGO)).thenReturn(Arrays.asList(prof));

        mockMvc.perform(get("/api/profissionais/categoria/PSICOLOGO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Dr. Bruno Costa"))
                .andExpect(jsonPath("$[0].categoria").value("PSICOLOGO"));
    }

    @Test
    void deveExcluirProfissionalComSucesso() throws Exception {
        when(repository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/profissionais/1"))
                .andExpect(status().isNoContent());
    }
}
