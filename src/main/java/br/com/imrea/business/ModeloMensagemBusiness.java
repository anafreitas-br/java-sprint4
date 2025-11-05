package br.com.imrea.business;

import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import br.com.imrea.repository.ModeloMensagemRepository;

import java.sql.SQLException;

/**
 * Regras de negócio para criação e gerenciamento de modelos de mensagem.
 *
 * - Somente administradores podem editar ou criar modelos
 * - Apenas um modelo pode estar ativo por vez
 * - Sempre que um novo modelo é criado, o anterior é desativado
 */
public class ModeloMensagemBusiness {
    private final ModeloMensagemRepository repo = new ModeloMensagemRepository();

    public ModeloMensagemBusiness() throws SQLException {}

    /**
     * Regra: apenas usuários com perfil ADMIN podem editar modelos de mensagem.
     */
    public boolean podeEditar(Usuario usuario) {
        return usuario.getPerfil() == PerfilUsuario.ADMIN;
    }

    /**
     * Regra: ao criar um novo modelo, o modelo anterior deve ser desativado.
     * Só pode haver um modelo ativo por vez.
     */
    public void criarNovoModelo(String nome, String conteudo) throws SQLException {
        repo.desativarModeloAtivo();
        repo.criarModelo(nome, conteudo);
    }

    /**
     * Regra: permite buscar o conteúdo de um modelo ativo pelo nome.
     */
    public String buscarModeloAtivo(String nome) throws SQLException {
        return repo.getConteudoByNomeAtivo(nome);
    }
}
