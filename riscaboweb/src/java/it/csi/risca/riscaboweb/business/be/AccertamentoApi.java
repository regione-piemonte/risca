package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;

/**
 * The interface Accertamento api.
 *
 * @author CSI PIEMONTE
 */
@Path("/accertamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface AccertamentoApi {
    @GET
    Response loadAllAccertamentiOrByIdStatoDeb(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idStatoDebitorio") String idStatoDebitorio,
    		@QueryParam(value = "offset")   String offset, 
    		@QueryParam(value = "limit")  String limit,
    		@DefaultValue("") @QueryParam(value = "sort") String sort,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveAccertamenti(@RequestBody AccertamentoExtendedDTO accertamento,@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAccertamenti(@RequestBody AccertamentoExtendedDTO accertamento, @QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @DELETE 
    @Path("/idAccertamento/{idAccertamento}")
    Response deleteByIdAccertamento(@PathParam("idAccertamento") String idAccertamento, @QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
