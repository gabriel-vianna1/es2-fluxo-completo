package com.agenda;

import com.agenda.model.ProfissionalSaude;
import com.agenda.model.Atendimento;
import com.agenda.model.ExameLaboratorio;
import com.agenda.model.Categoria;
import com.agenda.model.TipoReceita;
import com.agenda.repository.ProfissionalSaudeRepository;
import com.agenda.repository.AtendimentoRepository;
import com.agenda.repository.ExameLaboratorioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TESTES DE INTEGRAÇÃO - Saúde Web
 * Usa banco H2 em memória (application-test.properties)
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IntegracaoTest {

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private ExameLaboratorioRepository exameRepository;

    @Test
    void deveSalvarERecuperarProfissionalSaude() {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setNome("Dra. Carla Mendes");
        prof.setTelefone("31977778888");
        prof.setEndereco("Av. Saúde, 500");
        prof.setCategoria(Categoria.FISIOTERAPEUTA);

        ProfissionalSaude salvo = profissionalRepository.save(prof);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Dra. Carla Mendes");
        assertThat(salvo.getCategoria()).isEqualTo(Categoria.FISIOTERAPEUTA);
    }

    @Test
    void deveSalvarAtendimentoComProfissional() {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setNome("Dr. Roberto Faria");
        prof.setCategoria(Categoria.MEDICO);
        profissionalRepository.save(prof);

        Atendimento atendimento = new Atendimento();
        atendimento.setTitulo("Consulta Geral");
        atendimento.setData(LocalDate.of(2026, 8, 15));
        atendimento.setHorario(LocalTime.of(10, 0));
        atendimento.setLinkVideoConferencia("https://meet.google.com/xyz");
        atendimento.setTipoReceita(TipoReceita.REMEDIO);
        atendimento.setProfissional(prof);

        Atendimento salvo = atendimentoRepository.save(atendimento);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getProfissional().getNome()).isEqualTo("Dr. Roberto Faria");
        assertThat(salvo.getTipoReceita()).isEqualTo(TipoReceita.REMEDIO);
    }

    @Test
    void deveSalvarExameLaboratorioVinculadoAoAtendimento() {
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setNome("Dra. Lucia Pinto");
        prof.setCategoria(Categoria.MEDICO);
        profissionalRepository.save(prof);

        Atendimento atendimento = new Atendimento();
        atendimento.setTitulo("Consulta com exames");
        atendimento.setData(LocalDate.of(2026, 9, 1));
        atendimento.setProfissional(prof);
        atendimentoRepository.save(atendimento);

        ExameLaboratorio exame = new ExameLaboratorio();
        exame.setDescricao("Hemograma completo");
        exame.setPosologia("Jejum de 8 horas antes da coleta");
        exame.setAtendimento(atendimento);

        ExameLaboratorio salvo = exameRepository.save(exame);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getDescricao()).isEqualTo("Hemograma completo");
        assertThat(salvo.getAtendimento().getTitulo()).isEqualTo("Consulta com exames");
    }

    @Test
    void deveBuscarProfissionaisPorCategoria() {
        ProfissionalSaude p1 = new ProfissionalSaude();
        p1.setNome("Dr. Paulo Gomes");
        p1.setCategoria(Categoria.PSICOLOGO);
        profissionalRepository.save(p1);

        ProfissionalSaude p2 = new ProfissionalSaude();
        p2.setNome("Dra. Maria Souza");
        p2.setCategoria(Categoria.MEDICO);
        profissionalRepository.save(p2);

        var psicologos = profissionalRepository.findByCategoria(Categoria.PSICOLOGO);
        assertThat(psicologos).hasSize(1);
        assertThat(psicologos.get(0).getNome()).isEqualTo("Dr. Paulo Gomes");
    }
}
