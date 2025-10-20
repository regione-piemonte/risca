package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvvedimentoDTO;



/**
 * The interface Provvedimenti Istanze Api .
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ProvvedimentiIstanzeApi {
	

    /**
     * get Provvedimenti Istanze.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response list Provvedimenti Istanze
     */
	@GET
    @Path("/provvedimenti-istanze")
    Response getProvvedimentiIstanze( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
    /**
     * get Provvedimenti Istanze by Id Riscossione .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response list Provvedimenti Istanze
     * @throws Exception 
     */
    
    @GET
    @Path("/riscossioni/{idRiscossione}/provvedimenti-istanze")
    Response getProvvedimentiIstanzeByidRiscossione(@PathParam("idRiscossione") String idRiscossione,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
    /**
     * get Provvedimenti Istanze by Id Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    
    @GET
    @Path("/provvedimenti-istanze/{idProvvedimentiIstanze}")

    Response getProvvedimentoIstanzaByIdProvvedimenti(@PathParam("idProvvedimentiIstanze") String idProvvedimentiIstanze,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)throws Exception;
    /**
     * save Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    
    @POST
    @Path("/provvedimenti-istanze")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveProvvedimentiIstanze(@RequestBody ProvvedimentoDTO provvedimentoDTO,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)throws Exception;
    /**
     * update Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    @PUT
    @Path("/provvedimenti-istanze")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateProvvedimentiIstanze(@RequestBody  ProvvedimentoDTO provvedimentoDTO,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
    /**
     * delete Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    @DELETE
    @Path("/provvedimenti-istanze/{idProvvedimentiIstanze}")
    @Consumes(MediaType.APPLICATION_JSON)

    Response deleteProvvedimentiIstanze(@PathParam("idProvvedimentiIstanze") String idProvvedimentiIstanze,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)throws Exception;
 

}
