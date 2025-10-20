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
 * The interface Stati Elaborazione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface StatiElaborazioneApi {

    /**
     * Load stati elaborazione tramite idAmbito and idFunzionalita.
     *
     * @param idAmbito    idAmbito
     * @param idFunzionalita    idFunzionalita
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/stato-elaborazione")
    Response loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(@QueryParam("idAmbito")   String idAmbito,@QueryParam("idFunzionalita") String idFunzionalita,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
