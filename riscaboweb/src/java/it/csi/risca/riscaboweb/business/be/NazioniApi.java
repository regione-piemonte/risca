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
 * The interface Nazione api.
 *
 * @author CSI PIEMONTE
 */
@Path("/nazioni")
@Produces(MediaType.APPLICATION_JSON)
public interface NazioniApi {
	
	 /**
     * Load nazioni response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    Response loadNazioni(@QueryParam("attivo") boolean attivo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
