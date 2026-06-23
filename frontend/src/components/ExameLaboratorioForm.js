import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameService, atendimentoService } from '../services/api';

function ExameLaboratorioForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [atendimentos, setAtendimentos] = useState([]);
  const [form, setForm] = useState({
    descricao: '',
    posologia: '',
    atendimento: null
  });

  useEffect(() => {
    atendimentoService.listar().then(res => setAtendimentos(res.data));
    if (id) {
      exameService.buscar(id).then(res => {
        const e = res.data;
        setForm({
          descricao: e.descricao || '',
          posologia: e.posologia || '',
          atendimento: e.atendimento ? { id: e.atendimento.id } : null
        });
      });
    }
  }, [id]);

  const handleChange = e => {
    const { name, value } = e.target;
    if (name === 'atendimentoId') {
      setForm({ ...form, atendimento: value ? { id: Number(value) } : null });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      if (id) {
        await exameService.atualizar(id, form);
      } else {
        await exameService.criar(form);
      }
      navigate('/exames');
    } catch (error) {
      console.error('Erro ao salvar:', error);
      alert('Erro ao salvar exame.');
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar' : 'Novo'} Exame de Laboratório</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Descrição *</label>
          <textarea name="descricao" value={form.descricao} onChange={handleChange}
            rows={3} required />
        </div>
        <div className="form-group">
          <label>Posologia</label>
          <textarea name="posologia" value={form.posologia} onChange={handleChange}
            rows={3} placeholder="Ex: Jejum de 8 horas antes da coleta" />
        </div>
        <div className="form-group">
          <label>Atendimento</label>
          <select name="atendimentoId"
            value={form.atendimento?.id || ''}
            onChange={handleChange}>
            <option value="">Selecione...</option>
            {atendimentos.map(a => (
              <option key={a.id} value={a.id}>{a.titulo} — {a.data}</option>
            ))}
          </select>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <button type="button" className="btn" onClick={() => navigate('/exames')}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}

export default ExameLaboratorioForm;
