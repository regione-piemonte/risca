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
 * The interface TipoRicercaRimborsiApi.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoRicercaRimborsiApi {

	/**
	 * load All Tipo Ricerca Rimborsi.
	 * 
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-ricerca-rimborsi")
	Response loadAllTipoRicercaRimborsi(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	/**
	 * ricercaRimborsi.
	 * 
	 * @param fruitore            fruitore
	 * @param tipoRicercaRimborsi tipoRicercaRimborsi
	 * @param anno                anno
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ricerca_rimborsi")
	Response ricercaRimborsi(@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoRicercaRimborsi") String tipoRicercaRimborsi,
			@QueryParam("anno")  String anno,
			@QueryParam(value = "offset") String offset,
			@QueryParam(value = "limit")  String limit,
			@QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}
