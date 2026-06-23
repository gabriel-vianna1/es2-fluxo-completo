import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProfissionalSaudeList from './components/ProfissionalSaudeList';
import ProfissionalSaudeForm from './components/ProfissionalSaudeForm';
import AtendimentoList from './components/AtendimentoList';
import AtendimentoForm from './components/AtendimentoForm';
import ExameLaboratorioList from './components/ExameLaboratorioList';
import ExameLaboratorioForm from './components/ExameLaboratorioForm';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1>🏥 Saúde Web</h1>
          <div className="nav-links">
            <Link to="/profissionais">Profissionais</Link>
            <Link to="/atendimentos">Atendimentos</Link>
            <Link to="/exames">Exames</Link>
          </div>
        </nav>

        <main className="container">
          <Routes>
            <Route path="/" element={<ProfissionalSaudeList />} />
            <Route path="/profissionais" element={<ProfissionalSaudeList />} />
            <Route path="/profissionais/novo" element={<ProfissionalSaudeForm />} />
            <Route path="/profissionais/editar/:id" element={<ProfissionalSaudeForm />} />
            <Route path="/atendimentos" element={<AtendimentoList />} />
            <Route path="/atendimentos/novo" element={<AtendimentoForm />} />
            <Route path="/atendimentos/editar/:id" element={<AtendimentoForm />} />
            <Route path="/exames" element={<ExameLaboratorioList />} />
            <Route path="/exames/novo" element={<ExameLaboratorioForm />} />
            <Route path="/exames/editar/:id" element={<ExameLaboratorioForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
