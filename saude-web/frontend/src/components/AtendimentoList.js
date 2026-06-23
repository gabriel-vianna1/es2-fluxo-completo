import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { atendimentoService } from '../services/api';

const RECEITA_LABEL = {
  REMEDIO: '💊 Remédio',
  ATIVIDADE_FISICA: '🏃 Atividade Física',
  ATIVIDADE_MENTAL: '🧠 Atividade Mental'
};

function AtendimentoList() {
  const [atendimentos, setAtendimentos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarAtendimentos();
  }, []);

  const carregarAtendimentos = async () => {
    try {
      const response = await atendimentoService.listar();
      setAtendimentos(response.data);
    } catch (error) {
      console.error('Erro ao carregar atendimentos:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletar = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este atendimento?')) {
      try {
        await atendimentoService.deletar(id);
        carregarAtendimentos();
      } catch (error) {
        console.error('Erro ao deletar:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>📋 Atendimentos</h2>
        <Link to="/atendimentos/novo" className="btn btn-primary">+ Novo Atendimento</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Título</th>
            <th>Data</th>
            <th>Horário</th>
            <th>Profissional</th>
            <th>Receita</th>
            <th>Vídeo</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {atendimentos.map(a => (
            <tr key={a.id}>
              <td>{a.titulo}</td>
              <td>{a.data}</td>
              <td>{a.horario}</td>
              <td>{a.profissional?.nome || '-'}</td>
              <td>{RECEITA_LABEL[a.tipoReceita] || '-'}</td>
              <td>
                {a.linkVideoConferencia
                  ? <a href={a.linkVideoConferencia} target="_blank" rel="noreferrer">🔗 Link</a>
                  : '-'}
              </td>
              <td>
                <Link to={`/atendimentos/editar/${a.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletar(a.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {atendimentos.length === 0 && <p className="empty">Nenhum atendimento cadastrado.</p>}
    </div>
  );
}

export default AtendimentoList;
