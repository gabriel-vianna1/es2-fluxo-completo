package com.agenda.controller;

import com.agenda.model.ExameLaboratorio;
import com.agenda.repository.ExameLaboratorioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "*")
public class ExameLaboratorioController {

    @Autowired
    private ExameLaboratorioRepository repository;

    @GetMapping
    public List<ExameLaboratorio> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/atendimento/{atendimentoId}")
    public List<ExameLaboratorio> buscarPorAtendimento(@PathVariable Long atendimentoId) {
        return repository.findByAtendimentoId(atendimentoId);
    }

    @PostMapping
    public ExameLaboratorio inserir(@Valid @RequestBody ExameLaboratorio exame) {
        return repository.save(exame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> alterar(@PathVariable Long id,
                                                     @Valid @RequestBody ExameLaboratorio dados) {
        return repository.findById(id).map(e -> {
            e.setDescricao(dados.getDescricao());
            e.setPosologia(dados.getPosologia());
            e.setAtendimento(dados.getAtendimento());
            return ResponseEntity.ok(repository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
