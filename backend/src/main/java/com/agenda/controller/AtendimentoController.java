package com.agenda.controller;

import com.agenda.model.Atendimento;
import com.agenda.repository.AtendimentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin(origins = "*")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository repository;

    @GetMapping
    public List<Atendimento> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atendimento> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profissional/{profissionalId}")
    public List<Atendimento> buscarPorProfissional(@PathVariable Long profissionalId) {
        return repository.findByProfissionalId(profissionalId);
    }

    @PostMapping
    public Atendimento inserir(@Valid @RequestBody Atendimento atendimento) {
        return repository.save(atendimento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atendimento> alterar(@PathVariable Long id,
                                                @Valid @RequestBody Atendimento dados) {
        return repository.findById(id).map(a -> {
            a.setTitulo(dados.getTitulo());
            a.setData(dados.getData());
            a.setHorario(dados.getHorario());
            a.setLinkVideoConferencia(dados.getLinkVideoConferencia());
            a.setTipoReceita(dados.getTipoReceita());
            a.setProfissional(dados.getProfissional());
            return ResponseEntity.ok(repository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
