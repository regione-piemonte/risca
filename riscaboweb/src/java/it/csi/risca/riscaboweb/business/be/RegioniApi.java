package it.csi.risca.riscaboweb.business.be;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author CSI PIEMONTE
 */
@Path("")
@Produces({"application/json"})
public interface RegioniApi {
	
	 /**
     * Load Regioni response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/regioni")
    Response loadRegioni(  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    /**
     * Load Regione by cod regione response.
     *
     * @param cod regione     cod regione 
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/regioni/{codRegione}")
    Response loadRegioneByCodRegione(@PathParam("codRegione") String codRegione,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
