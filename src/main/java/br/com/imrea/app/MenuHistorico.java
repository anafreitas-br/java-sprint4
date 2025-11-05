package br.com.imrea.app;

import br.com.imrea.model.entities.HistoricoNotificacao;
import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import br.com.imrea.repository.HistoricoNotificacaoRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuHistorico {
    public static void exibir(Scanner scanner, Usuario usuario) {
        try {
            HistoricoNotificacaoRepository historicoRepo = new HistoricoNotificacaoRepository();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            System.out.println("\n--- HISTÓRICO ---");
            System.out.println("1. Ver histórico por lembrete");
            System.out.println("2. Excluir histórico");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 1) {
                System.out.print("ID do lembrete: ");
                long idLembrete = Long.parseLong(scanner.nextLine());
                List<HistoricoNotificacao> historicos = historicoRepo.findByLembrete(idLembrete);

                if (historicos.isEmpty()) {
                    System.out.println("Nenhum histórico encontrado.");
                } else {
                    for (HistoricoNotificacao h : historicos) {
                        System.out.printf("ID #%d | %s | %s | %s%n",
                                h.getId(),
                                h.getDataEvento().format(formatter),
                                h.getStatusNotificacao(),
                                h.getDetalhe());
                    }
                }

            } else if (opcao == 2) {
                if (usuario.getPerfil() != PerfilUsuario.ADMIN) {
                    System.out.println("❌ Somente administradores podem excluir históricos.");
                    return;
                }

                System.out.print("ID do histórico para excluir: ");
                long id = Long.parseLong(scanner.nextLine());

                System.out.print("Tem certeza que deseja excluir o histórico? (sim/não): ");
                String confirmacao = scanner.nextLine().trim().toLowerCase();

                if (confirmacao.equals("sim")) {
                    historicoRepo.delete(id);
                    System.out.println("✅ Histórico excluído.");
                } else {
                    System.out.println("❎ Exclusão cancelada.");
                }

            } else {
                System.out.println("Opção inválida.");
            }

        } catch (Exception e) {
            System.out.println("Erro no histórico: " + e.getMessage());
        }
    }
}
