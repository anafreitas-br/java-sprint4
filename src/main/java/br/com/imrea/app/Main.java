package br.com.imrea.app;

import br.com.imrea.business.UsuarioBusiness;
import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.PerfilUsuario;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            Usuario usuarioLogado = MenuUsuario.realizarLogin(scanner);

            exibirMenuPrincipal(scanner, usuarioLogado);

        } catch (Exception e) {
            System.out.println(" Erro inesperado: " + e.getMessage());
        }
    }

    private static void exibirMenuPrincipal(Scanner scanner, Usuario usuario) throws Exception {
        boolean executando = true;

        while (executando) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Consultas");
            System.out.println("2. Lembretes");
            System.out.println("3. Histórico de Notificações");
            System.out.println("4. Modelos de Mensagem");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Encerrando o sistema...");
                executando = false;
            } else if (input.equals("1")) {
                MenuConsulta.exibir(scanner);
            } else if (input.equals("2")) {
                MenuLembrete.exibir(scanner, usuario);
            } else if (input.equals("3")) {
                MenuHistorico.exibir(scanner, usuario);
            } else if (input.equals("4")) {
                if (usuario.getPerfil() == PerfilUsuario.ADMIN) {
                    MenuModeloMensagem.exibir(scanner);
                } else {
                    System.out.println("Acesso restrito a administradores.");
                }
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
