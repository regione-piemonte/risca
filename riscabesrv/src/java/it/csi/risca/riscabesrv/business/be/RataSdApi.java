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

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;

import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;

@Path("/rata-sd")
@Produces(MediaType.APPLICATION_JSON)
public interface RataSdApi {

	@GET
    Response loadRataSdByStatoDebitorio(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/{idStatoDebitorio}")
    Response saveNRataSd(@QueryParam("fruitore") String fruitore,
    		@PathParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
   @Context HttpServletRequest httpRequest) throws DAOException, GenericExceptionList;
   
	
	@POST
	@Path("/figlia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "Create a new rata sd", notes = "Create a new rata sd", response = RataSdDTO.class, tags={ "rataSd", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RataSdDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createRataSd(@RequestBody RataSdDTO rataSd, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);



}
