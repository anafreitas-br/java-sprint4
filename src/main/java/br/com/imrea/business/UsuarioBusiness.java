package br.com.imrea.business;

import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import br.com.imrea.repository.UsuarioRepository;

import java.sql.SQLException;

/**
 * Camada de regras de negócio relacionadas à entidade Usuario.
 * Responsável por validar autenticação e permissões de perfil.
 */
public class UsuarioBusiness {
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    public UsuarioBusiness() throws SQLException {
    }

    /**
     * Regra de negócio: autentica um usuário a partir do login e senha.
     * Se não encontrar o usuário, lança uma exceção indicando credenciais inválidas.
     */
    public Usuario autenticar(String login, String senha) throws SQLException {
        Usuario usuario = this.usuarioRepo.autenticar(login, senha);

        if (usuario == null) {
            // Regra de segurança: evitar acesso indevido e forçar credenciais válidas.
            throw new IllegalArgumentException("Login ou senha inválidos");
        } else {
            return usuario;
        }
    }

    /**
     * Regra de autorização: verifica se o usuário possui perfil ADMIN.
     * Essa verificação é usada para liberar ou bloquear funcionalidades sensíveis,
     * como exclusão de histórico ou edição de modelos de mensagem.
     */
    public boolean isAdmin(Usuario usuario) {
        return usuario.getPerfil() == PerfilUsuario.ADMIN;
    }
}

