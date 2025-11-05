package br.com.imrea.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModeloMensagemRepository {
    private Connection con = (new ConnectionFactory()).getConnection();

    public ModeloMensagemRepository() throws SQLException {
    }

    public String getConteudoByNomeAtivo(String nome) throws SQLException {
        String sql = "    SELECT conteudo\n    FROM ch_modelo_mensagens\n    WHERE nome = ?\n      AND ativo = 'S'\n";

        try (PreparedStatement ps = this.con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("conteudo");
            }
        }

        return null;
    }

    public void criarModelo(String nome, String conteudo) throws SQLException {
        String sql = "    INSERT INTO ch_modelo_mensagens (nome, conteudo, ativo)\n    VALUES (?, ?, 'S')\n";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, conteudo);
            pstmt.executeUpdate();
        }

    }

    public void atualizarModeloAtivo(String novoConteudo) throws SQLException {
        String sql = "UPDATE ch_modelo_mensagens SET conteudo = ? WHERE ativo = 'S'";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, novoConteudo);
            pstmt.executeUpdate();
        }

    }

    public void desativarModeloAtivo() throws SQLException {
        String sql = "UPDATE ch_modelo_mensagens SET ativo = 'N' WHERE ativo = 'S'";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }

    }

    public Connection getConnection() {
        return this.con;
    }
}

