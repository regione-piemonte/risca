package it.csi.risca.riscaboweb.business.be;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author CSI PIEMONTE
 */
@Path("")
@Produces({"application/json"})
public interface TipiRiscossioneApi {

	 /**
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-riscossione")
    Response getTipiRiscossione(@HeaderParam ("X-Request-ID") String XRequestId, @HeaderParam ("X-Forwarded-For") String XForwardedFor,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**

     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-riscossione")
    Response getTipiRiscossioneByIdAmbitoAndDateValidita(@PathParam("idAmbito") String idAmbito, @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo riscossione by idTipoRiscossione or cod TipoRiscossione and idAmbito.
     *
     * @param idTipoRiscossione idTipoRiscossione
     * @param idAmbito    idAmbito
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-riscossione/{idTipoRiscossione}")
    Response getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(@PathParam("idTipoRiscossione") String idOrCodTipoRiscossione, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo riscossione by codTipoRiscossione and idAmbito.
     *
     * @param codTipoRiscossione codTipoRiscossione
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/codice/{codTipoRiscossione}/id-ambito/{idAmbito}")
    Response getTipoRiscossioneByCodeAndIdAmbito(@PathParam("codTipoRiscossione") String codTipoRiscossione, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
