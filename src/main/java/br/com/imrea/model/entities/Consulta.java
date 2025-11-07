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

    // ✅ Construtor vazio (necessário para frameworks e serialização)
    public Consulta() {}

    // ✅ Construtor completo
    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataConsulta,
                    TipoConsulta tipoConsulta, StatusConsulta status) {

        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dataConsulta;
        this.tipoConsulta = tipoConsulta;
        this.status = status;

        // Log seguro — evita erro se paciente ou médico vierem nulos
        String nomePac = (paciente != null && paciente.getPessoa() != null)
                ? paciente.getPessoa().getNome()
                : "Sem nome de paciente";
        String nomeMed = (medico != null && medico.getPessoa() != null)
                ? medico.getPessoa().getNome()
                : "Sem nome de médico";

        System.out.println("🔹 Criando Consulta: " + nomePac + " | " + nomeMed);
    }

    // --- Getters e Setters ---
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDateTime getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDateTime dataConsulta) { this.dataConsulta = dataConsulta; }

    public TipoConsulta getTipoConsulta() { return tipoConsulta; }
    public void setTipoConsulta(TipoConsulta tipoConsulta) { this.tipoConsulta = tipoConsulta; }

    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }

    // --- Representação em texto ---
    @Override
    public String toString() {
        String nomePac = (paciente != null && paciente.getPessoa() != null)
                ? paciente.getPessoa().getNome()
                : "Sem nome de paciente";
        String nomeMed = (medico != null && medico.getPessoa() != null)
                ? medico.getPessoa().getNome()
                : "Sem nome de médico";
        String data = (dataConsulta != null)
                ? dataConsulta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "Data indefinida";

        return String.format("Consulta #%d | Paciente: %s | Médico: %s | Data: %s | Tipo: %s | Status: %s",
                id, nomePac, nomeMed, data, tipoConsulta, status);
    }
}

