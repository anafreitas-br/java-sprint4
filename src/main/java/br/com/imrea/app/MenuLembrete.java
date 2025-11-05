package br.com.imrea.app;

import br.com.imrea.model.entities.Consulta;
import br.com.imrea.model.entities.Lembrete;
import br.com.imrea.model.entities.Usuario;
import br.com.imrea.model.enums.CanalLembrete;
import br.com.imrea.model.enums.StatusLembrete;
import br.com.imrea.repository.ConsultaRepository;
import br.com.imrea.repository.HistoricoNotificacaoRepository;
import br.com.imrea.repository.LembreteRepository;
import br.com.imrea.repository.ModeloMensagemRepository;
import br.com.imrea.service.MensagemService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuLembrete {
    public static void exibir(Scanner scanner, Usuario usuario) {
        try {
            LembreteRepository lembreteRepo = new LembreteRepository();
            ConsultaRepository consultaRepo = new ConsultaRepository();
            HistoricoNotificacaoRepository historicoRepo = new HistoricoNotificacaoRepository();
            ModeloMensagemRepository modeloRepo = new ModeloMensagemRepository();
            DateTimeFormatter formatterBR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            System.out.println("\n--- MENU LEMBRETES ---");
            System.out.println("1. Criar lembrete");
            System.out.println("2. Editar lembrete");
            System.out.println("3. Excluir lembrete");
            System.out.println("4. Listar lembretes");
            System.out.println("5. Registrar resposta do paciente");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();
            int opcao;

            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" ⚠️ Digite apenas números!");
                return;
            }

            if (opcao == 1) {
                System.out.print("Informe o ID da consulta: ");
                long consultaId = Long.parseLong(scanner.nextLine());
                Consulta consulta = consultaRepo.findByIdComPacienteEMedico(consultaId);

                if (consulta == null) {
                    System.out.println("❌ Consulta não encontrada.");
                    return;
                }

                String modeloBase = modeloRepo.getConteudoByNomeAtivo("Lembrete de Consulta");
                String mensagemFinal;

                if (modeloBase != null) {
                    mensagemFinal = MensagemService.montarMensagem(modeloBase, consulta);
                    System.out.println("\nMensagem automática:");
                    System.out.println(mensagemFinal);

                    System.out.print("\nDeseja editar (S/N)? ");
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        System.out.print("Nova mensagem: ");
                        mensagemFinal = scanner.nextLine();
                    }
                } else {
                    System.out.print("⚠️ Modelo não encontrado. Digite a mensagem: ");
                    mensagemFinal = scanner.nextLine();
                }

                Lembrete novoLembrete = new Lembrete();
                novoLembrete.setConsulta(consulta);
                novoLembrete.setMensagem(mensagemFinal);
                novoLembrete.setStatusLembrete(StatusLembrete.PENDENTE);

                System.out.print("Data/hora de envio (dd/MM/yyyy HH:mm): ");
                novoLembrete.setDataEnvio(LocalDateTime.parse(scanner.nextLine(), formatterBR));

                System.out.print("Canal (SMS, EMAIL, WHATSAPP): ");
                novoLembrete.setCanal(CanalLembrete.valueOf(scanner.nextLine().toUpperCase()));

                lembreteRepo.save(novoLembrete);
                historicoRepo.registrarCriacao(novoLembrete);
                System.out.println("✅ Lembrete criado e histórico registrado com sucesso!");

            }

            else if (opcao == 2) {
                System.out.print("ID do lembrete para editar: ");
                long id = Long.parseLong(scanner.nextLine());
                Lembrete lembrete = lembreteRepo.findById(id);

                if (lembrete == null) {
                    System.out.println("❌ Lembrete não encontrado.");
                    return;
                }

                System.out.print("Nova mensagem: ");
                lembrete.setMensagem(scanner.nextLine());
                lembreteRepo.update(lembrete);
                historicoRepo.registrarEdicao(lembrete);
                System.out.println("✅ Lembrete atualizado com sucesso!");

            }

            else if (opcao == 3) {
                System.out.print("ID do lembrete para excluir: ");
                long idE = Long.parseLong(scanner.nextLine());

                System.out.print("Tem certeza que deseja excluir? (S/N): ");
                String confirmacao = scanner.nextLine().trim().toUpperCase();

                if (confirmacao.equals("S")) {
                    lembreteRepo.delete(idE);
                    System.out.println("✅ Lembrete excluído com sucesso!");
                } else {
                    System.out.println("❎ Exclusão cancelada.");
                }

            }

            else if (opcao == 4) {
                List<Lembrete> lembretes = lembreteRepo.findAll();
                if (lembretes.isEmpty()) {
                    System.out.println(" ⚠️ Nenhum lembrete encontrado.");
                } else {
                    for (Lembrete l : lembretes) {
                        System.out.printf("""
                                Lembrete #%d | Paciente: %s | Médico: %s | %s
                                Mensagem: %s
                                ------------------------------------------
                                """,
                                l.getId(),
                                l.getConsulta().getPaciente().getPessoa().getNome(),
                                l.getConsulta().getMedico().getPessoa().getNome(),
                                l.getDataEnvio().format(formatterBR),
                                l.getMensagem());
                    }
                }

            }

            else if (opcao == 5) {
                System.out.print("ID do lembrete: ");
                long idLembrete = Long.parseLong(scanner.nextLine());
                System.out.print("Resposta (SIM/NÃO): ");
                String resposta = scanner.nextLine();

                lembreteRepo.atualizarRespostaPaciente(idLembrete, resposta);
                historicoRepo.registrarResposta(idLembrete, resposta);
                System.out.println("✅ Resposta registrada e histórico atualizado!");

            }

            else {
                System.out.println("❌ Opção inválida. Tente novamente.");
            }

        } catch (Exception e) {
            System.out.println(" ⚠️ Erro no menu de lembretes: " + e.getMessage());
        }
    }
}
