package br.com.imrea.util;

import java.time.LocalDateTime;

public class ConsultaTO {

    private String nomePaciente;
    private String nomeMedico;
    private String especialidade;
    private LocalDateTime dataConsulta;

    public ConsultaTO(String nomePaciente, String nomeMedico, String especialidade, LocalDateTime dataConsulta) {
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.especialidade = especialidade;
        this.dataConsulta = dataConsulta;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

}
