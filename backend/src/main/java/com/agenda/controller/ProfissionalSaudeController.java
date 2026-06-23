package com.agenda.controller;

import com.agenda.model.ProfissionalSaude;
import com.agenda.model.Categoria;
import com.agenda.repository.ProfissionalSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalSaudeController {

    @Autowired
    private ProfissionalSaudeRepository repository;

    // Consultar todos / Consultar por nome
    @GetMapping
    public List<ProfissionalSaude> listar(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return repository.findByNomeContainingIgnoreCase(nome);
        }
        return repository.findAll();
    }

    // Consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Consultar por categoria
    @GetMapping("/categoria/{categoria}")
    public List<ProfissionalSaude> buscarPorCategoria(@PathVariable Categoria categoria) {
        return repository.findByCategoria(categoria);
    }

    // Inserir
    @PostMapping
    public ProfissionalSaude inserir(@Valid @RequestBody ProfissionalSaude profissional) {
        return repository.save(profissional);
    }

    // Alterar
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> alterar(@PathVariable Long id,
                                                      @Valid @RequestBody ProfissionalSaude dados) {
        return repository.findById(id).map(p -> {
            p.setNome(dados.getNome());
            p.setTelefone(dados.getTelefone());
            p.setEndereco(dados.getEndereco());
            p.setCategoria(dados.getCategoria());
            return ResponseEntity.ok(repository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Excluir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
