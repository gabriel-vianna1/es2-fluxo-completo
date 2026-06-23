import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [profissionais, setProfissionais] = useState([]);
  const [form, setForm] = useState({
    titulo: '',
    data: '',
    horario: '',
    linkVideoConferencia: '',
    tipoReceita: '',
    profissional: null
  });

  useEffect(() => {
    profissionalService.listar().then(res => setProfissionais(res.data));
    if (id) {
      atendimentoService.buscar(id).then(res => {
        const a = res.data;
        setForm({
          titulo: a.titulo || '',
          data: a.data || '',
          horario: a.horario || '',
          linkVideoConferencia: a.linkVideoConferencia || '',
          tipoReceita: a.tipoReceita || '',
          profissional: a.profissional ? { id: a.profissional.id } : null
        });
      });
    }
  }, [id]);

  const handleChange = e => {
    const { name, value } = e.target;
    if (name === 'profissionalId') {
      setForm({ ...form, profissional: value ? { id: Number(value) } : null });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      if (id) {
        await atendimentoService.atualizar(id, form);
      } else {
        await atendimentoService.criar(form);
      }
      navigate('/atendimentos');
    } catch (error) {
      console.error('Erro ao salvar:', error);
      alert('Erro ao salvar atendimento.');
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar' : 'Novo'} Atendimento</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Título *</label>
          <input name="titulo" value={form.titulo} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Data *</label>
          <input name="data" type="date" value={form.data} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Horário</label>
          <input name="horario" type="time" value={form.horario} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Link de Videoconferência</label>
          <input name="linkVideoConferencia" value={form.linkVideoConferencia}
            onChange={handleChange} placeholder="https://meet.google.com/..." />
        </div>
        <div className="form-group">
          <label>Tipo de Receita</label>
          <select name="tipoReceita" value={form.tipoReceita} onChange={handleChange}>
            <option value="">Nenhuma</option>
            <option value="REMEDIO">💊 Remédio (Médico)</option>
            <option value="ATIVIDADE_FISICA">🏃 Atividade Física (Fisioterapeuta)</option>
            <option value="ATIVIDADE_MENTAL">🧠 Atividade Mental (Psicólogo)</option>
          </select>
        </div>
        <div className="form-group">
          <label>Profissional</label>
          <select name="profissionalId"
            value={form.profissional?.id || ''}
            onChange={handleChange}>
            <option value="">Selecione...</option>
            {profissionais.map(p => (
              <option key={p.id} value={p.id}>{p.nome} — {p.categoria}</option>
            ))}
          </select>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}

export default AtendimentoForm;
