package br.com.imrea.resource;

import br.com.imrea.model.entities.Lembrete;
import br.com.imrea.model.enums.CanalLembrete;
import br.com.imrea.model.enums.Especialidade;
import br.com.imrea.model.enums.StatusLembrete;
import br.com.imrea.model.enums.TipoConsulta;
import br.com.imrea.repository.ConsultaRepository;
import br.com.imrea.repository.LembreteRepository;
import br.com.imrea.repository.ModeloMensagemRepository;
import br.com.imrea.service.MensagemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/lembretes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LembreteResource {

    private LembreteRepository repo;

    public LembreteResource() {
        try {
            this.repo = new LembreteRepository();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    @GET
    public Response listar() {
        try {
            List<Lembrete> lista = repo.findAll();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return Response.serverError()
                    .entity("Erro ao listar lembretes: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") long id) {
        try {
            Lembrete lembrete = repo.findById(id);

            if (lembrete == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Lembrete não encontrado")
                        .build();
            }

            Lembrete retorno = new Lembrete(
                    lembrete.getMensagem(),
                    lembrete.getDataEnvio(),
                    lembrete.getStatusLembrete(),
                    lembrete.getCanal(),
                    lembrete.getConsulta().getPaciente().getPessoa().getNome(),
                    lembrete.getConsulta().getPaciente().getCns(),
                    lembrete.getConsulta().getMedico().getPessoa().getNome(),
                    lembrete.getConsulta().getMedico().getCrm(),
                    Especialidade.valueOf(lembrete.getConsulta().getMedico().getEspecialidade().name()),
                    TipoConsulta.valueOf(lembrete.getConsulta().getTipoConsulta().name())
            );

            return Response.ok(retorno).build();

        } catch (SQLException e) {
            return Response.serverError()
                    .entity("Erro ao buscar lembrete: " + e.getMessage())
                    .build();
        }
    }


    @POST
    public Response criar(Lembrete lembrete) {
        try {

            var consultaRepo = new ConsultaRepository();
            var modeloRepo = new ModeloMensagemRepository();


            var consulta = consultaRepo.findByIdComPacienteEMedico(lembrete.getConsulta().getId());
            if (consulta == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Consulta não encontrada para o lembrete.")
                        .build();
            }


            String modeloBase = modeloRepo.getConteudoByNomeAtivo("Lembrete de Consulta");
            if (modeloBase == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Modelo de mensagem 'Lembrete de Consulta' não encontrado.")
                        .build();
            }


            String mensagemFinal = MensagemService.montarMensagem(modeloBase, consulta);

            if (lembrete.getCanal() == null) {
                lembrete.setCanal(CanalLembrete.SMS);
            }

            lembrete.setConsulta(consulta);
            lembrete.setMensagem(mensagemFinal);

            if (lembrete.getStatusLembrete() == null)
                lembrete.setStatusLembrete(StatusLembrete.PENDENTE);

            repo.save(lembrete);

            Lembrete retorno = new Lembrete(
                    lembrete.getMensagem(),
                    lembrete.getDataEnvio(),
                    lembrete.getStatusLembrete(),
                    lembrete.getCanal(),
                    lembrete.getNomePaciente(),
                    lembrete.getCnsPaciente(),
                    lembrete.getNomeMedico(),
                    lembrete.getCrmMedico(),
                    lembrete.getEspecialidade(),
                    lembrete.getTipoConsulta()
            );

            return Response.status(Response.Status.CREATED)
                    .entity(retorno)
                    .build();
        } catch (SQLException e) {
            return Response.serverError()
                    .entity("Erro ao criar lembrete: " + e.getMessage())
                    .build();
        }
    }


    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") long id, Lembrete lembrete) {
        try {
            lembrete.setId(id);
            repo.update(lembrete);
            return Response.ok("Lembrete atualizado com sucesso").build();
        } catch (SQLException e) {
            return Response.serverError()
                    .entity("Erro ao atualizar lembrete: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") long id) {
        try {
            repo.delete(id);
            return Response.ok("Lembrete excluído com sucesso").build();
        } catch (SQLException e) {
            return Response.serverError()
                    .entity("Erro ao excluir lembrete: " + e.getMessage())
                    .build();
        }
    }
}