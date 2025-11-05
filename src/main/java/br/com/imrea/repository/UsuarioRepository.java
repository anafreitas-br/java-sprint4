package br.com.imrea.repository;

import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {
    private Connection con = (new ConnectionFactory()).getConnection();

    public UsuarioRepository() throws SQLException {
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO ch_usuarios (nome, login, senha, perfil) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setString(1, usuario.getNomeUser());
            pstmt.setString(2, usuario.getLogin());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getPerfil().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
        }

    }

    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM ch_usuarios WHERE login = ? AND senha = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getLong("ID"), rs.getString("NOME_USER"), rs.getString("LOGIN"), rs.getString("SENHA"), PerfilUsuario.valueOf(rs.getString("PERFIL")));
            }
        }

        return null;
    }

    public Usuario findById(long id) throws SQLException {
        String sql = "SELECT * FROM ch_usuarios WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getLong("id"), rs.getString("nome_user"), rs.getString("login"), rs.getString("senha"), PerfilUsuario.valueOf(rs.getString("perfil")));
            }
        }

        return null;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE ch_usuarios SET nome = ?, login = ?, senha = ?, perfil = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNomeUser());
            pstmt.setString(2, usuario.getLogin());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getPerfil().name());
            pstmt.setLong(5, usuario.getId());
            pstmt.executeUpdate();
        }

    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM ch_usuarios WHERE id = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }

    }

    public Usuario findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM ch_usuarios WHERE login = ?";

        try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getLong("id"), rs.getString("nome_user"), rs.getString("login"), rs.getString("senha"), PerfilUsuario.valueOf(rs.getString("perfil")));
            }
        }

        return null;
    }
}

