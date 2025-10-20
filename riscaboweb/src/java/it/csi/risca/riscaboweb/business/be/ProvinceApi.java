package it.csi.risca.riscaboweb.business.be;

import javax.annotation.security.RolesAllowed;
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
 * The interface Province api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ProvinceApi {
	
	 /**
     * Load province by param attivo .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/province")
    Response getProvince(@QueryParam("attivo") boolean attivo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load province by codice istat comune.
     *
     * @param codIstatComune    codIstatComune
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/province/comune/{codIstatComune}")
    Response getProvinceByCodIstatComune(@PathParam("codIstatComune") String codIstatComune,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


    /**
     * Load provincia by id or codRegione e by id or codProvincia .
     *
     * @param codProvincia    codProvincia
     * @param codRegione    codRegione
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/regioni/{codRegione}/province/{codProvincia}")
    Response loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(@PathParam("codRegione") String codRegione, @PathParam("codProvincia") String codProvincia,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
    /**
     * Load province by codice regione.
     *
     * @param codRegione codRegione
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/regione/{codRegione}/province")
    Response getProvinceByCodRegione(@PathParam("codRegione") String codRegione,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
