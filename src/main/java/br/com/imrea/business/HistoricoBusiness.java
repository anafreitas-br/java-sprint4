package br.com.imrea.business;

import br.com.imrea.model.entities.HistoricoNotificacao;
import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import br.com.imrea.repository.HistoricoNotificacaoRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Classe responsável pelas regras de negócio relacionadas ao histórico de notificações.
 * Define critérios de permissão e elegibilidade para exclusão.
 */
public class HistoricoBusiness {

    private final HistoricoNotificacaoRepository repo = new HistoricoNotificacaoRepository();

    public HistoricoBusiness() throws SQLException {
    }

    /**
     * Verifica se o usuário logado tem permissão para excluir registros de histórico.
     *
     * Regra de negócio:
     * Apenas usuários com perfil ADMIN podem excluir históricos,
     * devido à criticidade da rastreabilidade das notificações.
     *
     */
    public boolean podeExcluir(Usuario usuario) {
        return usuario.getPerfil() == PerfilUsuario.ADMIN;
    }

    /**
     * Verifica se um histórico de notificação está elegível para exclusão com base na data de envio.
     *
     * Regra de negócio:
     * Apenas históricos associados a lembretes com data de envio passada (ou seja, já enviados)
     * podem ser excluídos. Notificações futuras não devem ser removidas.
     *
     */
    public boolean podeExcluirHistorico(HistoricoNotificacao h) {
        return h.getLembrete().getDataEnvio().isBefore(LocalDateTime.now());
    }
}
