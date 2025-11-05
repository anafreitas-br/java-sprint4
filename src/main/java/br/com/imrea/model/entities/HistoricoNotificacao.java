package br.com.imrea.model.entities;

import br.com.imrea.model.enums.StatusNotificacao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoNotificacao {
    private long id;
    private Lembrete lembrete;
    private LocalDateTime dataEvento;
    private StatusNotificacao statusNotificacao;
    private String detalhe;

    public HistoricoNotificacao() {
    }

    public HistoricoNotificacao(Lembrete lembrete, LocalDateTime dataEvento, StatusNotificacao statusNotificacao, String detalhe) {
        this.lembrete = lembrete;
        this.dataEvento = dataEvento;
        this.statusNotificacao = statusNotificacao;
        this.detalhe = detalhe;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Lembrete getLembrete() {
        return this.lembrete;
    }

    public void setLembrete(Lembrete lembrete) {
        this.lembrete = lembrete;
    }

    public LocalDateTime getDataEvento() {
        return this.dataEvento;
    }

    public void setDataEvento(LocalDateTime dataEvento) {
        this.dataEvento = dataEvento;
    }

    public StatusNotificacao getStatusNotificacao() {
        return this.statusNotificacao;
    }

    public void setStatusNoticacao(StatusNotificacao statusNotificacao) {
        this.statusNotificacao = statusNotificacao;
    }

    public String getDetalhe() {
        return this.detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public String toString() {
        String paciente = this.lembrete != null && this.lembrete.getConsulta() != null ? this.lembrete.getConsulta().getPaciente().getPessoa().getNome() : "N/A";
        String medico = this.lembrete != null && this.lembrete.getConsulta() != null ? this.lembrete.getConsulta().getMedico().getPessoa().getNome() : "N/A";
        return String.format("Histórico #%d | Lembrete #%d | Paciente: %s | Médico: %s | Data: %s | Status: %s | Detalhe: %s", this.id, this.lembrete != null ? this.lembrete.getId() : 0L, paciente, medico, this.dataEvento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), this.statusNotificacao, this.detalhe);
    }
}

