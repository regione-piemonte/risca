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
public interface UnitaMisuraApi {

	 /**
	 ** Load unita misura
	 * 
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/unita-misura")
    Response loadUnitaMisura( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
	 * Load unita misura by idAmbito
	 * 
     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/unita-misura/idAmbito/{idAmbito}")
    Response loadUnitaMisuraByIdAmbito(@PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load unita misura by keyUnitaMisura
     *
     * @param keyUnitaMisura keyUnitaMisura
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/unita-misura/keyUnitaMisura/{keyUnitaMisura}")
    Response loadUnitaMisuraByKeyUnitaMisura(@PathParam("keyUnitaMisura") String keyUnitaMisura,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load unita misura by idUnitaMisura.
     *
     * @param idUnitaMisura idUnitaMisura
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/unita-misura/{idUnitaMisura}")
    Response loadUnitaMisuraByIdUnitaMisura(@PathParam("idUnitaMisura") String idUnitaMisura,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
