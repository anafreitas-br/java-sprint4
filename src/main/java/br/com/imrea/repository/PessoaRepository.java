package br.com.imrea.repository;

import br.com.imrea.model.entities.Medico;
import br.com.imrea.model.entities.Paciente;
import br.com.imrea.model.entities.Pessoa;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.Sexo;
import br.com.imrea.model.enums.TelTipo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PessoaRepository {
    private final Connection con = (new ConnectionFactory()).getConnection();

    public PessoaRepository() throws SQLException {
    }

    private long savePessoa(Pessoa p) throws SQLException {
        String sql = "    INSERT INTO ch_pessoas (\n        nome, data_nascimento, sexo, ddd, tel_numero, tel_tipo,\n        email, logradouro, numero_endereco, complemento, bairro, cidade, estado, cep\n    )\n    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setString(1, p.getNome());
            pstmt.setDate(2, Date.valueOf(p.getDataNascimento()));
            pstmt.setString(3, p.getSexo().name());
            pstmt.setString(4, p.getDdd());
            pstmt.setString(5, p.getTelNumero());
            pstmt.setString(6, p.getTelTipo().name());
            pstmt.setString(7, p.getEmail());
            pstmt.setString(8, p.getLogradouro());
            pstmt.setString(9, p.getNumeroEndereco());
            pstmt.setString(10, p.getComplemento());
            pstmt.setString(11, p.getBairro());
            pstmt.setString(12, p.getCidade());
            pstmt.setString(13, p.getEstado());
            pstmt.setString(14, p.getCep());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Erro ao obter ID da pessoa");
            } else {
                long id = rs.getLong(1);
                p.setId(id);
                return id;
            }
        }
    }

    public Pessoa cadastrarPessoa(Pessoa p) throws SQLException {
        this.savePessoa(p);
        return p;
    }

    public void saveMedico(Medico medico) throws SQLException {
        long pessoaId = this.savePessoa(medico.getPessoa());
        String sql = "INSERT INTO ch_medicos (id, crm, especialidade) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, pessoaId);
            pstmt.setString(2, medico.getCrm());
            pstmt.setString(3, medico.getEspecialidade().name());
           pstmt.executeUpdate();
        }

        medico.setId(pessoaId);
    }

    public void savePaciente(Paciente paciente) throws SQLException {
        long pessoaId = this.savePessoa(paciente.getPessoa());
        String sql = "INSERT INTO ch_pacientes (id, cns) VALUES (?, ?)";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, pessoaId);
            pstmt.setString(2, paciente.getCns());
            pstmt.executeUpdate();
        }

        paciente.setId(pessoaId);
    }

    public Optional<Pessoa> findPessoaById(long id) throws SQLException {
        String sql = "SELECT * FROM ch_pessoas WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(this.construirPessoa(rs));
            }
        }

        return Optional.empty();
    }

    public Optional<Medico> findMedicoById(long id) throws SQLException {
        String sql = "    SELECT m.id as medico_id, m.crm, m.especialidade, p.*\n    FROM ch_medicos m\n    JOIN ch_pessoas p ON m.id = p.id\n    WHERE m.id = ?\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Medico m = new Medico();
                m.setId(rs.getLong("medico_id"));
                m.setCrm(rs.getString("crm"));
                m.setEspecialidade(Especialidade.valueOf(rs.getString("especialidade")));
                m.setPessoa(this.construirPessoa(rs));
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    public Optional<Paciente> findPacienteById(long id) throws SQLException {
        String sql = "    SELECT p.*, pac.cns\n    FROM ch_pacientes pac\n    JOIN ch_pessoas p ON pac.id = p.id\n    WHERE pac.id = ?\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Paciente pac = new Paciente();
                pac.setId(rs.getLong("id"));
                pac.setCns(rs.getString("cns"));
                pac.setPessoa(this.construirPessoa(rs));
                return Optional.of(pac);
            }
        }

        return Optional.empty();
    }

    public void updatePessoa(Pessoa p) throws SQLException {
        String sql = "    UPDATE ch_pessoas SET\n        nome=?, data_nascimento=?, sexo=?, ddd=?, tel_numero=?, tel_tipo=?,\n        email=?, logradouro=?, numero_endereco=?, complemento=?, bairro=?, cidade=?, estado=?, cep=?\n    WHERE id=?\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, p.getNome());
            pstmt.setDate(2, Date.valueOf(p.getDataNascimento()));
            pstmt.setString(3, p.getSexo().name());
            pstmt.setString(4, p.getDdd());
            pstmt.setString(5, p.getTelNumero());
            pstmt.setString(6, p.getTelTipo().name());
            pstmt.setString(7, p.getEmail());
            pstmt.setString(8, p.getLogradouro());
            pstmt.setString(9, p.getNumeroEndereco());
            pstmt.setString(10, p.getComplemento());
            pstmt.setString(11, p.getBairro());
            pstmt.setString(12, p.getCidade());
            pstmt.setString(13, p.getEstado());
            pstmt.setString(14, p.getCep());
            pstmt.setLong(15, p.getId());
            pstmt.executeUpdate();
        }

    }

    private Pessoa construirPessoa(ResultSet rs) throws SQLException {
        return new Pessoa(rs.getLong("id"), rs.getString("nome"), rs.getDate("data_nascimento").toLocalDate(), Sexo.valueOf(rs.getString("sexo")), rs.getString("ddd"), rs.getString("tel_numero"), TelTipo.valueOf(rs.getString("tel_tipo")), rs.getString("email"), rs.getString("logradouro"), rs.getString("numero_endereco"), rs.getString("complemento"), rs.getString("bairro"), rs.getString("cidade"), rs.getString("estado"), rs.getString("cep"));
    }

    public void deletePessoa(long id) throws SQLException {
        String sql = "DELETE FROM ch_pessoas WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }

    }

    public void deleteMedico(long id) throws SQLException {
        try (
                PreparedStatement pstmt1 = this.con.prepareStatement("DELETE FROM ch_medicos WHERE id = ?");
                PreparedStatement pstmt2 = this.con.prepareStatement("DELETE FROM ch_pessoas WHERE id = ?");
        ) {
            pstmt1.setLong(1, id);
            pstmt1.executeUpdate();
            pstmt2.setLong(1, id);
            pstmt2.executeUpdate();
        }

    }

    public void deletePaciente(long id) throws SQLException {
        try (
                PreparedStatement pstmt1 = this.con.prepareStatement("DELETE FROM ch_pacientes WHERE id = ?");
                PreparedStatement pstmt2 = this.con.prepareStatement("DELETE FROM ch_pessoas WHERE id = ?");
        ) {
            pstmt1.setLong(1, id);
            pstmt1.executeUpdate();
            pstmt2.setLong(1, id);
            pstmt2.executeUpdate();
        }

    }
}

