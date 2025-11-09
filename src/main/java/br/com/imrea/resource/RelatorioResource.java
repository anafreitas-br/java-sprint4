package br.com.imrea.resource;

import br.com.imrea.service.RelatorioService;
import br.com.imrea.util.RelatorioEspecialidadeTO;

import br.com.imrea.util.RelatorioPacienteTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelatorioResource {

    private final RelatorioService relatorioService;

    public RelatorioResource() throws SQLException {
        this.relatorioService = new RelatorioService();
    }

    @GET
    @Path("/especialidades")
    public Response getRelatorioPorEspecialidade(@QueryParam("inicio") String inicio,
                                                 @QueryParam("fim") String fim) {
        try {
            LocalDate dataInicio = LocalDate.parse(inicio);
            LocalDate dataFim = LocalDate.parse(fim);

            List<RelatorioEspecialidadeTO> relatorio = relatorioService.buscarRelatorioPorEspecialidade(dataInicio, dataFim);
            return Response.ok(relatorio).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar relatório: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/semanal")
    public Response getRelatorioSemanalComparativo() {
        try {
            return Response.ok(relatorioService.gerarComparativoSemanal()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar relatório semanal: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/pacientes")
    public Response getRelatorioPorPaciente(@QueryParam("inicio") String inicio,
                                            @QueryParam("fim") String fim) {
        try {
            LocalDate dataInicio = LocalDate.parse(inicio);
            LocalDate dataFim = LocalDate.parse(fim);
            List<RelatorioPacienteTO> relatorio = relatorioService.buscarRelatorioPorPaciente(dataInicio, dataFim);
            return Response.ok(relatorio).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar relatório por paciente: " + e.getMessage()).build();
        }
    }

}

