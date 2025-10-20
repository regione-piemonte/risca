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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.File450DTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;


@Path("/file-450")
@Produces(MediaType.APPLICATION_JSON)
public interface File450Api {

	/**
	 * Save file 450 response.
	 *
	 * @param file450  File450DTO
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response File450DTO
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "Create a new file450", notes = "Create a new file 450", response = File450DTO.class, tags={ "file450", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = File450DTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createFile450(@RequestBody File450DTO file450, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

/**
 * @param fruitore
 * @param tipoRicercaMorosita
 * @param listStatoDebitorioMorosita
 * @param securityContext
 * @param httpHeaders
 * @param httpRequest
 * @return
 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ruolo-ricerca-morosita")
	public Response creaRuoloRicercaMorosita(@QueryParam("fruitore") String fruitore,@QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita,
    		@QueryParam("anno") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer anno, 
    		@QueryParam("flgRest") @Min(value = 0, message = "flgRest deve essere 0 o 1") @Max(value = 1, message = "flgRest deve essere 0 o 1") Integer flgRest ,
    		@QueryParam("flgAnn")@Min(value = 0, message = "flgAnn deve essere 0 o 1") @Max(value = 1, message = "flgAnn deve essere 0 o 1") Integer flgAnn,
    		@QueryParam("lim") String lim,List<Long> listIdStatiDebitoriSelezionati,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	

}
