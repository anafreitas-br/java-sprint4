package br.com.imrea.business;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.enums.StatusConsulta;
import java.time.LocalDateTime;

/**
 * Camada de regras de negócio da entidade Consulta.
 *
 * Responsável por validar elegibilidade para lembretes, consistência de status e
 * cronograma das consultas médicas.
 */
public class ConsultaBusiness {

    /**
     * Verifica se uma consulta ainda está apta a receber lembretes.
     *
     * ✔️ Regra de negócio:
     * - Consultas com status CANCELADA ou REALIZADA não podem receber novos lembretes.
     *
     */
    public boolean podeGerarLembrete(Consulta consulta) {
        return consulta.getStatus() != StatusConsulta.CANCELADA &&
                consulta.getStatus() != StatusConsulta.REALIZADA;
    }

    /**
     * Atualiza o status da consulta com base na resposta do paciente ao lembrete.
     *
     *  Regra de negócio:
     * - "SIM" → CONFIRMADA
     * - "NAO" → CANCELADA
     * - Outros valores não alteram o status
     *
     */
    public void atualizarStatusPorResposta(Consulta consulta, String resposta) {
        if ("SIM".equalsIgnoreCase(resposta)) {
            consulta.setStatus(StatusConsulta.CONFIRMADA);
        } else if ("NAO".equalsIgnoreCase(resposta)) {
            consulta.setStatus(StatusConsulta.CANCELADA);
        }
    }

    /**
     * Verifica se a data da consulta ainda está no futuro.
     *
     * Regra de negócio:
     * - Apenas consultas futuras são consideradas válidas para envio de lembretes.
     *
     */
    public boolean dataValida(Consulta consulta) {
        return consulta.getDataConsulta().isAfter(LocalDateTime.now());
    }
}
