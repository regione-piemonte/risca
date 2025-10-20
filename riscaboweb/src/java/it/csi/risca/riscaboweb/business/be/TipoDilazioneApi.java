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
 * The interface Tipo Dilazione api.
 *
 * @author CSI PIEMONTE
 */
@Path("/dilazione")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoDilazioneApi {

    /**
     * Load tipi dilazione tramite idAmbito.
     *
     * @param idAmbito    idAmbito
     * @param idTipoDilazione idTipoDilazione
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/ambito/{idAmbito}")
    Response loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(@PathParam("idAmbito") String idAmbito, @QueryParam("idTipoDilazione") String idTipoDilazione,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
