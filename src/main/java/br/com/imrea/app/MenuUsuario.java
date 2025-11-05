package br.com.imrea.app;

import br.com.imrea.business.UsuarioBusiness;
import br.com.imrea.model.entities.Usuario;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuUsuario {

    public static Usuario realizarLogin(Scanner scanner)throws SQLException {
        UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
        Usuario usuarioLogado = null;

        while (usuarioLogado == null) {
            System.out.println("===== LOGIN =====");
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            usuarioLogado = usuarioBusiness.autenticar(login, senha);

            if (usuarioLogado == null) {
                System.out.println("❌ Login ou senha inválidos. Tente novamente.\n");
            } else {
                System.out.printf("✅ Bem-vindo, %s (%s)%n",
                        usuarioLogado.getNomeUser(),
                        usuarioLogado.getPerfil());
            }
        }

        return usuarioLogado;
    }
}
