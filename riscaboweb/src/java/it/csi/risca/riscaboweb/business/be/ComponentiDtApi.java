package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface ComponentiDt api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ComponentiDtApi {
	
	 /**
     * Load comuni response.
     *
     * @param idAmbito        idAmbito
     * @param codTipoComponente codTipoComponente
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/ambiti/{idAmbito}/componenti-dt")
    Response loadComponentiDt(@PathParam("idAmbito") String idAmbito, @QueryParam("codTipoComponente") String codTipoComponente, @QueryParam("attivo") boolean attivo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
