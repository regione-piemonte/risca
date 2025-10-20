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
 * The interface Tipi Natura Giuridica api.
 *
 * @author CSI PIEMONTE
 */
@Path("/tipi-natura-giuridica")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiNaturaGiuridicaApi {

	 /**
     * Load tipi natura giuridica response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    Response loadTipiNaturaGiuridica( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
	 /**
     * Load tipo natura giuridica by id or cod response.
     *
     * @param idTipoNaturaGiuridica idTipoNaturaGiuridica
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/{idTipoNaturaGiuridica}")
    Response loadTipoNaturaGiuridicaByIdOrCod(@PathParam("idTipoNaturaGiuridica") String idTipoNaturaGiuridica,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
