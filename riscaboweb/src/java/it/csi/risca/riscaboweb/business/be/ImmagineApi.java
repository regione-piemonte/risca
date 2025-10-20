package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ImmagineDTO;

/**
 * The interface Immagine api.
 *
 * @author CSI PIEMONTE
 */
@Path("/immagine")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ImmagineApi {


	@POST
	Response saveImmagine(@RequestBody ImmagineDTO immagine,@QueryParam("fruitore") String fruitore, 
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    @GET
    Response loadImmagineById(@QueryParam("fruitore") String fruitore,@QueryParam("idImmagine") String idImmagine,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
