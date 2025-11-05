package br.com.imrea.app;

import br.com.imrea.repository.ModeloMensagemRepository;
import java.util.Scanner;

public class MenuModeloMensagem {
    public static void exibir(Scanner scanner) {
        try {
            ModeloMensagemRepository modeloRepo = new ModeloMensagemRepository();

            System.out.println("\n--- MENU MODELO DE MENSAGEM ---");
            System.out.println("1. Ver modelo ativo");
            System.out.println("2. Editar modelo ativo");
            System.out.println("3. Criar novo modelo");
            System.out.println("4. Desativar modelo ativo");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();
            int opcao;

            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Digite apenas números!");
                return;
            }

            if (opcao == 1) {
                String modeloAtivo = modeloRepo.getConteudoByNomeAtivo("Lembrete de Consulta");
                if (modeloAtivo != null) {
                    System.out.println("\n Modelo ativo:\n" + modeloAtivo);
                } else {
                    System.out.println("⚠️ Nenhum modelo ativo encontrado.");
                }

            } else if (opcao == 2) {
                System.out.println("Digite o novo texto do modelo:");
                String novo = scanner.nextLine();
                modeloRepo.atualizarModeloAtivo(novo);
                System.out.println("✅ Modelo atualizado com sucesso!");

            } else if (opcao == 3) {
                System.out.print("Nome do modelo: ");
                String nome = scanner.nextLine();
                System.out.print("Conteúdo do modelo: ");
                String conteudo = scanner.nextLine();
                modeloRepo.criarModelo(nome, conteudo);
                System.out.println("✅ Novo modelo criado com sucesso!");

            } else if (opcao == 4) {
                System.out.print("Tem certeza que deseja desativar o modelo ativo? (S/N): ");
                String confirm = scanner.nextLine().trim().toUpperCase();

                if (confirm.equals("S")) {
                    modeloRepo.desativarModeloAtivo();
                    System.out.println("✅ Modelo desativado com sucesso!");
                } else {
                    System.out.println("❎ Ação cancelada.");
                }

            } else {
                System.out.println("❌ Opção inválida. Tente novamente.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Erro no menu de modelo: " + e.getMessage());
        }
    }
}
