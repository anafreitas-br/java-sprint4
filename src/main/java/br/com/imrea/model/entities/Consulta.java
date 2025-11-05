package br.com.imrea.model.entities;

import br.com.imrea.model.enums.StatusConsulta;
import br.com.imrea.model.enums.TipoConsulta;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {
    private long id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataConsulta;
    private TipoConsulta tipoConsulta;
    private StatusConsulta status;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataConsulta, TipoConsulta tipoConsulta, StatusConsulta status) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dataConsulta;
        this.tipoConsulta = tipoConsulta;
        this.status = status;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDateTime getDataConsulta() {
        return this.dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public TipoConsulta getTipoConsulta() {
        return this.tipoConsulta;
    }

    public void setTipoConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public StatusConsulta getStatus() {
        return this.status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public String toString() {
        return String.format("Consulta #%d | Paciente: %s | Médico: %s | Data: %s | Tipo: %s | Status: %s", this.id, this.paciente.getPessoa().getNome(), this.medico.getPessoa().getNome(), this.dataConsulta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), this.tipoConsulta, this.status);
    }
}
