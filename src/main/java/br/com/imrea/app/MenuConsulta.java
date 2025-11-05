package br.com.imrea.app;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.repository.ConsultaRepository;
import java.util.List;
import java.util.Scanner;

public class MenuConsulta {
    public static void exibir(Scanner scanner) {
        try {
            ConsultaRepository consultaRepo = new ConsultaRepository();
            List<Consulta> consultas = consultaRepo.findAllComDadosCompletos();
            System.out.println("\n--- CONSULTAS ---");
            if (consultas.isEmpty()) {
                System.out.println("Nenhuma consulta encontrada.");
            } else {
                for(Consulta c : consultas) {
                    System.out.printf("ID: %d | Paciente: %s | Médico: %s (%s) | Data: %s\n", c.getId(), c.getPaciente().getPessoa().getNome(), c.getMedico().getPessoa().getNome(), c.getMedico().getEspecialidade(), c.getDataConsulta());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar consultas: " + e.getMessage());
        }

    }
}
