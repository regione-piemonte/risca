package it.csi.risca.riscaboweb.business.be;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CreaUsoRegolaDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoRegolaExtendedDTO;

/**
 * The interface Tipi uso Regola api.
 *
 * @author CSI PIEMONTE
 */
@Path("/tipi-usi-regole")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoUsoRegolaApi {

	/**
	 * Load TipoUsoRegolaApi.
	 * @param idAmbito     idAmbito
	 * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/anni/{idAmbito}")
	Response loadAllAnniFromDTInizio(@PathParam("idAmbito") String idAmbito,@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * loadAllUsoRegolaByIdAmbitoAndAnno.
	 * @param idAmbito     idAmbito
	 * @param anno     anno
     * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/lista-regole/{idAmbito}/{anno}")
	Response loadAllUsoRegolaByIdAmbitoAndAnno(@PathParam("idAmbito") String idAmbito,@PathParam("anno") String anno, 
			@QueryParam("fruitore") String fruitore,
			@QueryParam(value = "offset") String offset,
			@QueryParam(value = "limit") String limit,
			@QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * updateTipoUsoRegola.
	 * @RequestBody TipoUsoRegolaExtendedDTO     tipoUsoRegola
	 * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@PUT
	Response updateTipoUsoRegola(@RequestBody TipoUsoRegolaExtendedDTO  tipoUsoRegola,@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * saveTipoUsoRegola.
	 * @param CreaUsoRegolaDTO     usoRegola
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@POST
	Response saveTipoUsoRegola(@QueryParam("fruitore") String fruitore,@RequestBody CreaUsoRegolaDTO usoRegola,
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * updateAllTipoUsoRegola.
	 * @RequestBody List<TipoUsoRegolaExtendedDTO>     tipoUsoRegola
	 * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@PUT
	@Path("/lista-regole")
	Response updateAllTipoUsoRegola(@RequestBody List<TipoUsoRegolaExtendedDTO>  tipoUsoRegola, @QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
