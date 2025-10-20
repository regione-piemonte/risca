package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface Stampa Pratica Api .
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface StampaRiscossioneApi {

    /**
     * stampaRiscossione.
     *
     * @param idRiscossione  idRiscossione
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response FileDowload
     */
    @GET
    @Path("/stampa-riscossione/{idRiscossione}")
    Response stampaRiscossione(@PathParam("idRiscossione") String idRiscossione, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
}
