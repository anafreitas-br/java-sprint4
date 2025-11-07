package br.com.imrea.resource;

import br.com.imrea.util.ConsultaTO;
import br.com.imrea.model.entities.Consulta;
import br.com.imrea.repository.ConsultaRepository;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;


@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)

public class ConsultaResource {

    @GET
    public Response listarConsultas() {
        try {
            ConsultaRepository repository = new ConsultaRepository();
            List<ConsultaTO> consultas = repository.findAllResumo();
            return consultas.isEmpty()
                    ? Response.status(Response.Status.NO_CONTENT).build()
                    : Response.ok(consultas).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar consultas: " + e.getMessage())
                    .build();
        }
    }
}
