package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LockRiscossioneDTO;

/**
 * The interface LockRiscossioneApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/lock-riscossione")
@Produces(MediaType.APPLICATION_JSON)
public interface LockRiscossioneApi {

	@GET
	@Path("/all")
    Response getAllLockRiscossione(@QueryParam("fruitore") String fruitore,
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
    Response getLockRiscossioneByIdRiscossione(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idRiscossione")  String idRiscossione, 
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveLockRiscossione(@RequestBody LockRiscossioneDTO lockRiscossioneDTO, @QueryParam("fruitore") String fruitore,
			 @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	
	@DELETE
    Response deleteLockRiscossione(
    		@QueryParam("idRiscossione") String idRiscossione, @QueryParam("fruitore") String fruitore,
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
}
