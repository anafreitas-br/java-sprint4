package br.com.imrea.repository;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.entities.Lembrete;
import br.com.imrea.model.entities.Medico;
import br.com.imrea.model.entities.Paciente;
import br.com.imrea.model.entities.Pessoa;
import br.com.imrea.model.enums.CanalLembrete;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.StatusConsulta;
import br.com.imrea.model.enums.StatusLembrete;
import br.com.imrea.model.enums.TipoConsulta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LembreteRepository {
    private final Connection con = (new ConnectionFactory()).getConnection();

    public LembreteRepository() throws SQLException {
    }

    public void save(Lembrete lembrete) throws SQLException {
        String sql = " INSERT INTO ch_lembretes (consulta_id, mensagem, data_envio, status, canal, resposta_paciente)\n VALUES (?, ?, ?, ?, ?, ?)\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setLong(1, lembrete.getConsulta().getId());
            pstmt.setString(2, lembrete.getMensagem());
            pstmt.setTimestamp(3, Timestamp.valueOf(lembrete.getDataEnvio()));
            pstmt.setString(4, lembrete.getStatusLembrete().name());
            pstmt.setString(5, lembrete.getCanal().name());
            pstmt.setString(6, lembrete.getRespostaPaciente());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lembrete.setId(rs.getLong(1));
                }
            }
        }

    }

    public void update(Lembrete lembrete) throws SQLException {
        String sql = "    UPDATE ch_lembretes\n    SET mensagem = ?, data_envio = ?, status = ?, canal = ?\n    WHERE id = ?\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, lembrete.getMensagem());
            pstmt.setTimestamp(2, Timestamp.valueOf(lembrete.getDataEnvio()));


            String status = lembrete.getStatusLembrete() != null
                    ? lembrete.getStatusLembrete().name()
                    : "PENDENTE";

            pstmt.setString(3, status);
            pstmt.setString(4, lembrete.getCanal().name());
            pstmt.setLong(5, lembrete.getId());
            pstmt.executeUpdate();
        }
    }


    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM ch_lembretes WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }

    }

    public Lembrete findById(Long id) throws SQLException {
        String sql = """
        SELECT
            l.id AS lembrete_id,
            l.mensagem,
            l.data_envio,
            l.status,
            l.canal,
            l.resposta_paciente,
            c.id AS consulta_id,
            c.data_consulta,
            c.tipo_consulta,
            c.status_consulta,
            pa.id AS paciente_id,
            pa.cns,
            p.nome AS nome_paciente,
            m.id AS medico_id,
            m.crm,
            m.especialidade,
            pm.nome AS nome_medico
        FROM ch_lembretes l
        JOIN ch_consultas c ON l.consulta_id = c.id
        JOIN ch_pacientes pa ON c.paciente_id = pa.id
        JOIN ch_pessoas p ON pa.id = p.id
        JOIN ch_medicos m ON c.medico_id = m.id
        JOIN ch_pessoas pm ON m.id = pm.id
        WHERE l.id = ?
    """;

        try (
             PreparedStatement pstmt = this.con.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Pessoa pessoaPaciente = new Pessoa();
                pessoaPaciente.setNome(rs.getString("nome_paciente"));
                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                paciente.setCns(rs.getString("cns"));
                paciente.setPessoa(pessoaPaciente);
                Pessoa pessoaMedico = new Pessoa();
                pessoaMedico.setNome(rs.getString("nome_medico"));

                Medico medico = new Medico();
                medico.setId(rs.getLong("medico_id"));
                medico.setCrm(rs.getString("crm"));

                String especialidadeStr = rs.getString("especialidade");
                if (especialidadeStr != null) {
                    medico.setEspecialidade(Especialidade.valueOf(especialidadeStr));
                }
                medico.setPessoa(pessoaMedico);

                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
                consulta.setPaciente(paciente);
                consulta.setMedico(medico);

                String tipoConsultaStr = rs.getString("tipo_consulta");
                if (tipoConsultaStr != null) {
                    consulta.setTipoConsulta(TipoConsulta.valueOf(tipoConsultaStr));
                }

                Lembrete lembrete = new Lembrete();
                lembrete.setId(rs.getLong("lembrete_id"));
                lembrete.setMensagem(rs.getString("mensagem"));
                lembrete.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());
                lembrete.setStatusLembrete(StatusLembrete.valueOf(rs.getString("status")));
                lembrete.setCanal(CanalLembrete.valueOf(rs.getString("canal")));
                lembrete.setRespostaPaciente(rs.getString("resposta_paciente"));
                lembrete.setConsulta(consulta);

                return lembrete;
            }
        }

        return null;
    }

    public List<Lembrete> findAll() throws SQLException {
        List<Lembrete> lembretes = new ArrayList<>();
        String sql = """
        SELECT 
            l.id AS lembrete_id,
            l.mensagem,
            l.data_envio,
            l.status,
            l.canal,
            p.nome AS nome_paciente,
            pa.cns AS cns_paciente,
            pm.nome AS nome_medico,
            m.crm,
            m.especialidade,
            c.tipo_consulta
        FROM ch_lembretes l
        JOIN ch_consultas c ON l.consulta_id = c.id
        JOIN ch_pacientes pa ON c.paciente_id = pa.id
        JOIN ch_pessoas p ON pa.id = p.id
        JOIN ch_medicos m ON c.medico_id = m.id
        JOIN ch_pessoas pm ON m.id = pm.id
        ORDER BY l.data_envio DESC
    """;

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Lembrete lembrete = new Lembrete();
                lembrete.setId(rs.getLong("lembrete_id"));
                lembrete.setMensagem(rs.getString("mensagem"));
                lembrete.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());
                lembrete.setStatusLembrete(StatusLembrete.valueOf(rs.getString("status")));
                lembrete.setCanal(CanalLembrete.valueOf(rs.getString("canal")));

                lembrete.setNomePaciente(rs.getString("nome_paciente"));
                lembrete.setCnsPaciente(rs.getString("cns_paciente"));
                lembrete.setNomeMedico(rs.getString("nome_medico"));
                lembrete.setCrmMedico(rs.getString("crm"));
                lembrete.setEspecialidade(Especialidade.valueOf(rs.getString("especialidade")));
                lembrete.setTipoConsulta(TipoConsulta.valueOf(rs.getString("tipo_consulta")));

                lembretes.add(lembrete);
            }
        }

        return lembretes;
    }


    public void atualizarRespostaPaciente(long id, String resposta) throws SQLException {
        String sql = "UPDATE ch_lembretes SET resposta_paciente = ?, status = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, resposta.toUpperCase());
            if ("SIM".equalsIgnoreCase(resposta)) {
                pstmt.setString(2, StatusLembrete.CONFIRMADO.name());
            } else if (!"NAO".equalsIgnoreCase(resposta) && !"NÃO".equals(resposta)) {
                pstmt.setString(2, StatusLembrete.PENDENTE.name());
            } else {
                pstmt.setString(2, StatusLembrete.FALHOU.name());
            }

            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        }

    }

    public boolean findByConsultaId(long consultaId) throws SQLException {
        String sql = """
        SELECT 1
          FROM ch_lembretes
         WHERE consulta_id = ?
         FETCH FIRST 1 ROWS ONLY
    """;

        boolean lembreteEncontrado;

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, consultaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                lembreteEncontrado = rs.next();
            }
        }

        return lembreteEncontrado;
    }
}
