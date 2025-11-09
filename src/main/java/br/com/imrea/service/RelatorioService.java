package br.com.imrea.service;

import br.com.imrea.repository.ConnectionFactory;
import br.com.imrea.util.RelatorioEspecialidadeTO;
import br.com.imrea.util.RelatorioPacienteTO;
import br.com.imrea.util.RelatorioSemanalTO;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {
    private Connection con = (new ConnectionFactory()).getConnection();

    public RelatorioService() throws SQLException {
    }

    public List<RelatorioEspecialidadeTO> buscarRelatorioPorEspecialidade(LocalDate inicio, LocalDate fim) throws SQLException {
        List<RelatorioEspecialidadeTO> lista = new ArrayList<>();

        String sql = """
        SELECT m.especialidade,
               COUNT(*) AS total,
               SUM(CASE WHEN c.status_consulta = 'CONFIRMADA' THEN 1 ELSE 0 END) AS confirmadas,
               SUM(CASE WHEN c.status_consulta = 'REALIZADA' THEN 1 ELSE 0 END) AS realizadas,
               SUM(CASE WHEN c.status_consulta = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas
        FROM ch_consultas c
        JOIN ch_medicos m ON c.medico_id = m.id
        WHERE c.data_consulta BETWEEN ? AND ?
        GROUP BY m.especialidade
        ORDER BY m.especialidade
    """;

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(inicio));
            pstmt.setDate(2, Date.valueOf(fim));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioEspecialidadeTO dto = new RelatorioEspecialidadeTO();
                    dto.setNome(rs.getString("especialidade"));
                    dto.setConsultas(rs.getInt("total"));
                    dto.setConfirmadas(rs.getInt("confirmadas"));
                    dto.setRealizadas(rs.getInt("realizadas"));
                    dto.setCanceladas(rs.getInt("canceladas"));
                    lista.add(dto);
                }
            }
        }

        return lista;
    }
    public List<RelatorioSemanalTO> gerarComparativoSemanal() {
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate inicioAtual = hoje.with(DayOfWeek.MONDAY);
            LocalDate fimAtual = inicioAtual.plusDays(6);
            LocalDate inicioAnt = inicioAtual.minusWeeks(1);
            LocalDate fimAnt = inicioAnt.plusDays(6);

            List<RelatorioEspecialidadeTO> listaAtual = buscarRelatorioPorEspecialidade(inicioAtual, fimAtual);
            List<RelatorioEspecialidadeTO> listaAnterior = buscarRelatorioPorEspecialidade(inicioAnt, fimAnt);

            List<RelatorioSemanalTO> resultado = new ArrayList<>();

            for (RelatorioEspecialidadeTO atual : listaAtual) {
                RelatorioSemanalTO dto = new RelatorioSemanalTO();
                dto.setNome(atual.getNome());
                dto.setAtual(atual);


                RelatorioEspecialidadeTO anterior = listaAnterior.stream()
                        .filter(a -> a.getNome().equals(atual.getNome()))
                        .findFirst()
                        .orElse(new RelatorioEspecialidadeTO());

                dto.setAnterior(anterior);
                resultado.add(dto);
            }


            for (RelatorioEspecialidadeTO anterior : listaAnterior) {
                boolean jaExiste = resultado.stream()
                        .anyMatch(r -> r.getNome().equals(anterior.getNome()));
                if (!jaExiste) {
                    RelatorioSemanalTO dto = new RelatorioSemanalTO();
                    dto.setNome(anterior.getNome());
                    dto.setAtual(new RelatorioEspecialidadeTO());
                    dto.setAnterior(anterior);
                    resultado.add(dto);
                }
            }

            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
    public List<RelatorioPacienteTO> buscarRelatorioPorPaciente(LocalDate inicio, LocalDate fim) throws SQLException {
        List<RelatorioPacienteTO> lista = new ArrayList<>();

        String sql = """
        SELECT pes.nome AS paciente,
               COUNT(*) AS total,
               SUM(CASE WHEN c.status_consulta = 'REALIZADA' THEN 1 ELSE 0 END) AS realizadas,
               SUM(CASE WHEN c.status_consulta = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas,
               SUM(CASE WHEN c.status_consulta = 'NAO_COMPARECEU' THEN 1 ELSE 0 END) AS nao_compareceu
        FROM ch_consultas c
        JOIN ch_pacientes p ON c.paciente_id = p.id
        JOIN ch_pessoas pes ON p.id = pes.id
        WHERE c.data_consulta BETWEEN ? AND ?
        GROUP BY pes.nome
        ORDER BY pes.nome
    """;

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(inicio));
            pstmt.setDate(2, Date.valueOf(fim));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioPacienteTO dto = new RelatorioPacienteTO();
                    dto.setNome(rs.getString("paciente"));
                    dto.setTotal(rs.getInt("total"));
                    dto.setRealizadas(rs.getInt("realizadas"));
                    dto.setCanceladas(rs.getInt("canceladas"));
                    dto.setNaoCompareceu(rs.getInt("nao_compareceu"));
                    lista.add(dto);
                }
            }
        }

        return lista;
    }

}
