package it.csi.risca.riscaboweb.business.be;

import java.text.ParseException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TassiDiInteresseDTO;

/**
 * The interface EtichetteApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/configurazioni-elementi-applicativi/idAmbito")
@Produces(MediaType.APPLICATION_JSON)
public interface EtichetteApi {

	/**
	 * Load TassiDiInteresseApi.
	 * @param idAmbito     idAmbito
	 * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */

	@GET
	@Path("/{idAmbito}")
	Response loadConfigurazioniElementiApplicativi(
			@PathParam("idAmbito") Long idAmbito,
			@QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders, 
			@Context HttpServletRequest httpRequest
			);
	
}
