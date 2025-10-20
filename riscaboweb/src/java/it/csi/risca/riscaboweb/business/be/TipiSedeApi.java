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
 * The interface Tipi sede api.
 *
 * @author CSI PIEMONTE
 */
@Path("/tipi-sede")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiSedeApi {

	 /**
     * Load tipi sede response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    Response loadTipiSede(@QueryParam("TipoSoggetto") String tipoSoggetto,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
