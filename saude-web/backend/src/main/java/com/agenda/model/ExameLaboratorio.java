package com.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exames_laboratorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String posologia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
