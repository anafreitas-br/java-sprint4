package br.com.imrea.service;

import br.com.imrea.model.entities.Consulta;
import java.time.format.DateTimeFormatter;

public class MensagemService {
    public static String montarMensagem(String template, Consulta consulta) {
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return template.replace("{nome_paciente}", consulta.getPaciente().getPessoa().getNome()).replace("{nome_medico}", consulta.getMedico().getPessoa().getNome()).replace("{especialidade}", consulta.getMedico().getEspecialidade().toString()).replace("{data_consulta}", consulta.getDataConsulta().toLocalDate().format(dataFormatter)).replace("{hora_consulta}", consulta.getDataConsulta().toLocalTime().format(horaFormatter));
    }
}
