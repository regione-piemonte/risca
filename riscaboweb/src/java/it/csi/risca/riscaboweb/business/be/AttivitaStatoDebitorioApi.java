package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * The interface attivita stato debitorio api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface AttivitaStatoDebitorioApi {

	 /**
     * Load attivita stato debitorio response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@Path("/attivita-stato-deb")
    @GET
    Response getAttivitaStatoDeb(@QueryParam("tipoAttivita") String tipoAttivita,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
}
