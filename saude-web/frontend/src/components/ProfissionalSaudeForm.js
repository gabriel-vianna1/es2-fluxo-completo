import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalSaudeForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({
    nome: '',
    telefone: '',
    endereco: '',
    categoria: 'MEDICO'
  });

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id).then(res => setForm(res.data));
    }
  }, [id]);

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      if (id) {
        await profissionalService.atualizar(id, form);
      } else {
        await profissionalService.criar(form);
      }
      navigate('/profissionais');
    } catch (error) {
      console.error('Erro ao salvar:', error);
      alert('Erro ao salvar profissional.');
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar' : 'Novo'} Profissional de Saúde</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Nome *</label>
          <input name="nome" value={form.nome} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Telefone</label>
          <input name="telefone" value={form.telefone} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Endereço</label>
          <input name="endereco" value={form.endereco} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Categoria *</label>
          <select name="categoria" value={form.categoria} onChange={handleChange} required>
            <option value="MEDICO">Médico</option>
            <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
            <option value="PSICOLOGO">Psicólogo</option>
          </select>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <button type="button" className="btn" onClick={() => navigate('/profissionais')}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}

export default ProfissionalSaudeForm;
