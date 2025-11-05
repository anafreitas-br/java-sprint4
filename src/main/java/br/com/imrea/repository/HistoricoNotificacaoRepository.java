package br.com.imrea.repository;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.entities.HistoricoNotificacao;
import br.com.imrea.model.entities.Lembrete;
import br.com.imrea.model.entities.Medico;
import br.com.imrea.model.entities.Paciente;
import br.com.imrea.model.entities.Pessoa;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.StatusNotificacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoNotificacaoRepository {
    private Connection con = (new ConnectionFactory()).getConnection();

    public HistoricoNotificacaoRepository() throws SQLException {
    }

    public void save(HistoricoNotificacao historico) throws SQLException {
        String sql = "INSERT INTO ch_historico_notificacoes (lembrete_id, data_evento, status_notificacao, detalhe) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setLong(1, historico.getLembrete().getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(historico.getDataEvento() != null ? historico.getDataEvento() : LocalDateTime.now()));
            pstmt.setString(3, historico.getStatusNotificacao().name());
            pstmt.setString(4, historico.getDetalhe());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getLong(1));
                }
            }
        }

    }

    public void registrarCriacao(Lembrete lembrete) throws SQLException {
        String sql = "    INSERT INTO ch_historico_notificacoes (lembrete_id, data_evento, status_notificacao, detalhe)\n    VALUES (?, ?, ?, ?)\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, lembrete.getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, StatusNotificacao.CRIADO.name());
            pstmt.setString(4, "Lembrete criado e aguardando envio via " + String.valueOf(lembrete.getCanal()));
            pstmt.executeUpdate();
        }

    }

    public void registrarEdicao(Lembrete lembrete) throws SQLException {
        String sql = "    INSERT INTO ch_historico_notificacoes (lembrete_id, data_evento, status_notificacao, detalhe)\n    VALUES (?, ?, ?, ?)\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, lembrete.getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, StatusNotificacao.EDITADO.name());
            pstmt.setString(4, "Lembrete atualizado pelo usuário.");
            pstmt.executeUpdate();
        }

    }

    public void registrarResposta(long idLembrete, String resposta) throws SQLException {
        String sql = "    INSERT INTO ch_historico_notificacoes (lembrete_id, data_evento, status_notificacao, detalhe)\n    VALUES (?, ?, ?, ?)\n";
        StatusNotificacao status;
        String detalhe;
        if ("SIM".equalsIgnoreCase(resposta)) {
            status = StatusNotificacao.CONFIRMADO;
            detalhe = "Paciente confirmou presença na consulta.";
        } else if (!"NAO".equalsIgnoreCase(resposta) && !"NÃO".equalsIgnoreCase(resposta)) {
            status = StatusNotificacao.ERRO;
            detalhe = "Resposta inválida ou não reconhecida.";
        } else {
            status = StatusNotificacao.CANCELADO;
            detalhe = "Paciente informou que não poderá comparecer.";
        }

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, idLembrete);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, status.name());
            pstmt.setString(4, detalhe);
            pstmt.executeUpdate();
        }

    }

    public HistoricoNotificacao findById(long id) throws SQLException {
        String sql = "SELECT id, lembrete_id, data_evento, status_notificacao, detalhe FROM ch_historico_notificacoes WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                HistoricoNotificacao h = new HistoricoNotificacao();
                h.setId(rs.getLong("id"));
                h.setDataEvento(rs.getTimestamp("data_evento").toLocalDateTime());
                h.setStatusNoticacao(StatusNotificacao.valueOf(rs.getString("status_notificacao")));
                h.setDetalhe(rs.getString("detalhe"));
                Lembrete lembrete = new Lembrete();
                lembrete.setId(rs.getLong("lembrete_id"));
                h.setLembrete(lembrete);
                return h;
            }
        }

        return null;
    }

    public List<HistoricoNotificacao> findByLembrete(long lembreteId) throws SQLException {
        List<HistoricoNotificacao> historicos = new ArrayList();
        String sql = "    SELECT h.id, h.data_evento, h.status_notificacao, h.detalhe,\n           l.id AS lembrete_id, c.id AS consulta_id,\n           p.nome AS nome_paciente, pm.nome AS nome_medico, m.especialidade\n    FROM ch_historico_notificacoes h\n    JOIN ch_lembretes l ON h.lembrete_id = l.id\n    JOIN ch_consultas c ON l.consulta_id = c.id\n    JOIN ch_pacientes pa ON c.paciente_id = pa.id\n    JOIN ch_pessoas p ON pa.id = p.id\n    JOIN ch_medicos m ON c.medico_id = m.id\n    JOIN ch_pessoas pm ON m.id = pm.id\n    WHERE h.lembrete_id = ?\n    ORDER BY h.data_evento DESC\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, lembreteId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Pessoa pessoaPaciente = new Pessoa();
                pessoaPaciente.setNome(rs.getString("nome_paciente"));
                Paciente paciente = new Paciente();
                paciente.setPessoa(pessoaPaciente);
                Pessoa pessoaMedico = new Pessoa();
                pessoaMedico.setNome(rs.getString("nome_medico"));
                Medico medico = new Medico();
                medico.setPessoa(pessoaMedico);
                medico.setEspecialidade(Especialidade.valueOf(rs.getString("especialidade")));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                consulta.setPaciente(paciente);
                consulta.setMedico(medico);
                Lembrete lembrete = new Lembrete();
                lembrete.setId(rs.getLong("lembrete_id"));
                lembrete.setConsulta(consulta);
                HistoricoNotificacao h = new HistoricoNotificacao();
                h.setId(rs.getLong("id"));
                h.setDataEvento(rs.getTimestamp("data_evento").toLocalDateTime());
                h.setStatusNoticacao(StatusNotificacao.valueOf(rs.getString("status_notificacao")));
                h.setDetalhe(rs.getString("detalhe"));
                h.setLembrete(lembrete);
                historicos.add(h);
            }
        }

        return historicos;
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM ch_historico_notificacoes WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }

    }
}

