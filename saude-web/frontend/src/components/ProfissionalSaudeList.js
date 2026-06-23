import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

const CATEGORIA_LABEL = {
  MEDICO: 'Médico',
  FISIOTERAPEUTA: 'Fisioterapeuta',
  PSICOLOGO: 'Psicólogo'
};

function ProfissionalSaudeList() {
  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroCategoria, setFiltroCategoria] = useState('');

  useEffect(() => {
    carregarProfissionais();
  }, [filtroCategoria]);

  const carregarProfissionais = async () => {
    try {
      const response = filtroCategoria
        ? await profissionalService.listarPorCategoria(filtroCategoria)
        : await profissionalService.listar();
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletar = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este profissional?')) {
      try {
        await profissionalService.deletar(id);
        carregarProfissionais();
      } catch (error) {
        console.error('Erro ao deletar:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>👨‍⚕️ Profissionais de Saúde</h2>
        <Link to="/profissionais/novo" className="btn btn-primary">+ Novo Profissional</Link>
      </div>

      <div style={{ marginBottom: '1rem' }}>
        <label>Filtrar por categoria: </label>
        <select value={filtroCategoria} onChange={e => setFiltroCategoria(e.target.value)}>
          <option value="">Todos</option>
          <option value="MEDICO">Médico</option>
          <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
          <option value="PSICOLOGO">Psicólogo</option>
        </select>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>Categoria</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {profissionais.map(p => (
            <tr key={p.id}>
              <td>{p.nome}</td>
              <td>{p.telefone}</td>
              <td>{p.endereco}</td>
              <td>{CATEGORIA_LABEL[p.categoria] || p.categoria}</td>
              <td>
                <Link to={`/profissionais/editar/${p.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletar(p.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {profissionais.length === 0 && <p className="empty">Nenhum profissional cadastrado.</p>}
    </div>
  );
}

export default ProfissionalSaudeList;
