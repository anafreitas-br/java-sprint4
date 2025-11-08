package br.com.imrea.repository;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.entities.Medico;
import br.com.imrea.model.entities.Paciente;
import br.com.imrea.model.entities.Pessoa;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.StatusConsulta;
import br.com.imrea.model.enums.TipoConsulta;
import br.com.imrea.util.ConsultaTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {
    private Connection con = (new ConnectionFactory()).getConnection();

    public ConsultaRepository() throws SQLException {
    }

    public void save(Consulta consulta) throws SQLException {
        String sql = "INSERT INTO ch_consultas (paciente_id, medico_id, data_consulta, tipo_consulta, status_consulta ) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setLong(1, consulta.getPaciente().getId());
            pstmt.setLong(2, consulta.getMedico().getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(consulta.getDataConsulta()));
            pstmt.setString(4, consulta.getTipoConsulta().name());
            pstmt.setString(5, consulta.getStatus().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getLong(1));
                }
            }
        }

    }

    public Consulta findById(long id) throws SQLException {
        String sql = "SELECT * FROM ch_consultas WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Consulta c = new Consulta();
                c.setId(rs.getLong("id"));
                c.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
                c.setTipoConsulta(TipoConsulta.valueOf(rs.getString("tipo_consulta")));
                c.setStatus(StatusConsulta.valueOf(rs.getString("status_consulta")));
                return c;
            } else {
                return null;
            }
        }
    }

    public List<Consulta> findAllComDadosCompletos() throws SQLException {
        List<Consulta> consultas = new ArrayList();
        String sql = """
                SELECT\s
                  c.id AS consulta_id,\s
                  c.data_consulta,\s
                  c.tipo_consulta,\s
                  c.status_consulta,
                  p.id AS paciente_id, pes_p.nome AS nome_paciente,
                  m.id AS medico_id, pes_m.nome AS nome_medico, m.especialidade
                FROM ch_consultas c
                JOIN ch_pacientes p ON c.paciente_id = p.id
                JOIN ch_pessoas pes_p ON p.id = pes_p.id
                JOIN ch_medicos m ON c.medico_id = m.id
                JOIN ch_pessoas pes_m ON m.id = pes_m.id
                
                """;

        try (PreparedStatement stmt = this.con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Pessoa pessoaPaciente = new Pessoa();
                pessoaPaciente.setNome(rs.getString("nome_paciente"));
                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                paciente.setPessoa(pessoaPaciente);
                Pessoa pessoaMedico = new Pessoa();
                pessoaMedico.setNome(rs.getString("nome_medico"));
                Medico medico = new Medico();
                medico.setId(rs.getLong("medico_id"));
                medico.setPessoa(pessoaMedico);
                medico.setEspecialidade(Especialidade.valueOf(rs.getString("especialidade")));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
                consulta.setTipoConsulta(TipoConsulta.valueOf(rs.getString("tipo_consulta")));
                consulta.setStatus(StatusConsulta.valueOf(rs.getString("status_consulta")));
                consulta.setPaciente(paciente);
                consulta.setMedico(medico);
                consultas.add(consulta);
            }
        }

        return consultas;
    }

    public Consulta findByIdComPacienteEMedico(long id) throws SQLException {
        String sql = "SELECT c.id as consulta_id, c.data_consulta, c.tipo_consulta, c.status_consulta,\n                   p.id as paciente_id, pes_p.nome as nome_paciente,\n                   m.id as medico_id, pes_m.nome as nome_medico, m.especialidade\n            FROM ch_consultas c\n            JOIN ch_pacientes p ON c.paciente_id = p.id\n            JOIN ch_pessoas pes_p ON p.id = pes_p.id\n            JOIN ch_medicos m ON c.medico_id = m.id\n            JOIN ch_pessoas pes_m ON m.id = pes_m.id\n             WHERE c.id = ?\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pessoa pessoaPaciente = new Pessoa();
                pessoaPaciente.setNome(rs.getString("nome_paciente"));
                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                paciente.setPessoa(pessoaPaciente);
                Pessoa pessoaMedico = new Pessoa();
                pessoaMedico.setNome(rs.getString("nome_medico"));
                Medico medico = new Medico();
                medico.setId(rs.getLong("medico_id"));
                medico.setPessoa(pessoaMedico);
                medico.setEspecialidade(Especialidade.valueOf(rs.getString("especialidade")));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
                consulta.setTipoConsulta(TipoConsulta.valueOf(rs.getString("tipo_consulta")));
                consulta.setStatus(StatusConsulta.valueOf(rs.getString("status_consulta")));
                consulta.setPaciente(paciente);
                consulta.setMedico(medico);
                return consulta;
            }
        }

        return null;
    }

    public List<ConsultaTO> findAllResumo() throws SQLException {
        String sql = """
        SELECT pes_p.nome AS nome_paciente,
               pes_m.nome AS nome_medico,
               m.especialidade AS especialidade,
               c.data_consulta
        FROM ch_consultas c
        JOIN ch_pacientes p  ON c.paciente_id = p.id
        JOIN ch_pessoas  pes_p ON p.id = pes_p.id
        JOIN ch_medicos  m ON c.medico_id = m.id
        JOIN ch_pessoas  pes_m ON m.id = pes_m.id
        ORDER BY c.data_consulta ASC
    """;

        List<ConsultaTO> lista = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ConsultaTO(
                        rs.getString("nome_paciente"),
                        rs.getString("nome_medico"),
                        rs.getString("especialidade"),
                        rs.getTimestamp("data_consulta").toLocalDateTime()
                ));
            }
        }
        return lista;
    }

    public void updateStatus(long id, StatusConsulta statusConsulta) throws SQLException {
        String sql = "UPDATE ch_consultas SET status_consulta = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, statusConsulta.name());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        }

    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM ch_consultas WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }

    }
}
