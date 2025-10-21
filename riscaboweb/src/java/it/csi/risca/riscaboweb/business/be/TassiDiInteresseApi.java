/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
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
 * The interface TassiDiInteresseApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/tassi-interesse")
@Produces(MediaType.APPLICATION_JSON)
public interface TassiDiInteresseApi {

	/**
	 * Load TassiDiInteresseApi.
	 * @param idAmbito     idAmbito
	 * @param fruitore     fruitore
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */

	@GET
	@Path("")
	Response loadTassiDiInteresse(
			@QueryParam("idAmbito") String idAmbito, 
			@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoDiInteresse") String tipoDiInteresse,
			@QueryParam(value = "offset") String  offset,  
    		@QueryParam(value = "limit") String limit, 
    		@QueryParam(value = "sort") String sort,
			@Context HttpHeaders httpHeaders, 
			@Context HttpServletRequest httpRequest
			);
	
	//PermitAll usata per testare
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveTassiDiInteresse(
			@RequestBody TassiDiInteresseDTO tassiDiInteresse,
			@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest
			);
	
	@DELETE
	@Path("/idAmbitoInteresse/{idAmbitoInteresse}")
    Response deleteTassiDiInteresse(@QueryParam("fruitore") String fruitore,
    		@PathParam("idAmbitoInteresse") 
    		@Min(value = 1, message = "L'id deve essere maggiore di 0")  
    		@Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  String idAmbitoInteresse, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateTassiDiInteresse(
			@RequestBody TassiDiInteresseDTO tassiDiInteresse,
			@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, 
			@Context HttpServletRequest httpRequest)
					throws ParseException;
}
