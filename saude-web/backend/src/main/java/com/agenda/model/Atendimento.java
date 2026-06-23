package com.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Column(length = 200, nullable = false)
    private String titulo;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    private LocalTime horario;

    @Column(name = "link_video_conferencia", length = 500)
    private String linkVideoConferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_receita")
    private TipoReceita tipoReceita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissional;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
