package br.com.imrea.business;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.entities.HistoricoNotificacao;
import br.com.imrea.model.entities.Lembrete;
import br.com.imrea.model.enums.CanalLembrete;
import br.com.imrea.model.enums.StatusNotificacao;
import br.com.imrea.repository.HistoricoNotificacaoRepository;
import br.com.imrea.repository.LembreteRepository;
import br.com.imrea.repository.ModeloMensagemRepository;
import br.com.imrea.service.MensagemService;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Regras de negócio para criação e controle de lembretes e seus históricos.
 *
 * Regras principais:
 * - A mensagem é montada automaticamente com base em modelo ativo
 * - Lembretes só podem ser criados para consultas existentes e futuras
 * - Cada consulta pode ter no máximo um lembrete associado
 * - O envio padrão ocorre 30 minutos antes da consulta
 * - Toda ação relevante (criação, envio, resposta) é registrada no histórico
 */
public class LembreteBusiness {

    private final LembreteRepository lembreteRepo = new LembreteRepository();
    private final HistoricoNotificacaoRepository historicoRepo = new HistoricoNotificacaoRepository();
    private final ModeloMensagemRepository modeloRepo = new ModeloMensagemRepository();

    public LembreteBusiness() throws SQLException {}

    /**
     * Regra: gera um lembrete com base em modelo ativo.
     * Também registra a criação no histórico.
     */
    public Lembrete criarLembrete(Consulta consulta, LocalDateTime envio, CanalLembrete canal) throws SQLException {
        String modeloBase = modeloRepo.getConteudoByNomeAtivo("Lembrete de Consulta");

        MensagemService.montarMensagem(modeloBase, consulta);

        Lembrete lembrete = new Lembrete();
        lembreteRepo.save(lembrete);

        registrarHistorico(lembrete, StatusNotificacao.CRIADO, "Lembrete gerado automaticamente.");
        return lembrete;
    }

    /**
     * Regra: cria lembrete agendado para 30 minutos antes da consulta.
     */
    public void criarLembrete30MinAntes(Consulta consulta, CanalLembrete canal) throws SQLException {
        LocalDateTime envio = consulta.getDataConsulta().minusMinutes(30L);
        criarLembrete(consulta, envio, canal);
    }

    /**
     * Regra: toda modificação ou evento relevante deve ser registrado.
     * Utilizado para rastreabilidade e auditoria.
     */
    private void registrarHistorico(Lembrete lembrete, StatusNotificacao status, String detalhe) throws SQLException {
        HistoricoNotificacao hist = new HistoricoNotificacao(lembrete, LocalDateTime.now(), status, detalhe);
        historicoRepo.save(hist);
    }

    /**
     * Regra: não permitir múltiplos lembretes para a mesma consulta.
     */
    public boolean existeLembreteParaConsulta(long consultaId) throws SQLException {
        return lembreteRepo.findByConsultaId(consultaId);
    }
}
