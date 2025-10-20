package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TracciamentoDTO;


@Path("/tracciamento")
@Produces(MediaType.APPLICATION_JSON)
public interface TracciamentoApi {

	
	    @POST
	    @Consumes(MediaType.APPLICATION_JSON)
	    Response saveTracciamento(@RequestBody TracciamentoDTO tracciamento,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
