/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

import it.csi.risca.riscabesrv.dto.CreaUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;


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
	Response loadAllAnniFromDTInizio(@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer idAmbito,
			@QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * Load loadAllUsoRegolaByIdAmbitoAndAnno.
	 * @param idAmbito     idAmbito
	 * @param anno     anno
     * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/lista-regole/{idAmbito}/{anno}")
	Response loadAllUsoRegolaByIdAmbitoAndAnno(@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer idAmbito,
			@PathParam("anno") @Min(value = 1, message = "L'anno deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'anno deve essere minore o uguale a max value integer")  Integer anno,
    		@QueryParam(value = "offset") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer offset,
    		@QueryParam(value = "limit") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer limit, 
    		@QueryParam(value = "sort") String sort,
			@QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders,
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
	Response updateTipoUsoRegola(@RequestBody TipoUsoRegolaExtendedDTO  tipoUsoRegola, @QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	/**
	 * saveTipoUsoRegola.
	 * @param CreaUsoRegolaDTO usoRegola,
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@POST
	Response saveTipoUsoRegola(@QueryParam("fruitore") String fruitore, CreaUsoRegolaDTO usoRegola,
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
