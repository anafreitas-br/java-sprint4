package br.com.imrea.model.entities;

import br.com.imrea.model.enums.CanalLembrete;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.StatusLembrete;
import br.com.imrea.model.enums.TipoConsulta;


import java.time.LocalDateTime;

public class Lembrete {
    private long id;
    private Consulta consulta;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private StatusLembrete statusLembrete;
    private CanalLembrete canal;
    private String respostaPaciente;
    private String nomePaciente;
    private String cnsPaciente;
    private String nomeMedico;
    private String crmMedico;
    private Especialidade especialidade;
    private TipoConsulta tipoConsulta;

    public Lembrete() {
    }

    public Lembrete(String mensagem, LocalDateTime dataEnvio, StatusLembrete statusLembrete, CanalLembrete canal, String nomePaciente, String cnsPaciente, String nomeMedico, String crmMedico, Especialidade especialidade, TipoConsulta tipoConsulta) {
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.statusLembrete = statusLembrete;
        this.canal = canal;
        this.nomePaciente = nomePaciente;
        this.cnsPaciente = cnsPaciente;
        this.nomeMedico = nomeMedico;
        this.crmMedico = crmMedico;
        this.especialidade = especialidade;
        this.tipoConsulta = tipoConsulta;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Consulta getConsulta() {
        return this.consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return this.dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public StatusLembrete getStatusLembrete() {
        return this.statusLembrete;
    }

    public void setStatusLembrete(StatusLembrete statusLembrete) {
        this.statusLembrete = statusLembrete;
    }

    public CanalLembrete getCanal() {
        return this.canal;
    }

    public void setCanal(CanalLembrete canal) {
        this.canal = canal;
    }

    public String getRespostaPaciente() {
        return this.respostaPaciente;
    }

    public void setRespostaPaciente(String respostaPaciente) {
        this.respostaPaciente = respostaPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getCnsPaciente() {
        return cnsPaciente;
    }

    public void setCnsPaciente(String cnsPaciente) {
        this.cnsPaciente = cnsPaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getCrmMedico() {
        return crmMedico;
    }

    public void setCrmMedico(String crmMedico) {
        this.crmMedico = crmMedico;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public TipoConsulta getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
}