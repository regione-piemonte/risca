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

import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;


@Path("/recapiti")
@Produces(MediaType.APPLICATION_JSON)
public interface RecapitiApi {

	/**
	 * Save recapiti response.
	 *
	 * @param recapito  RecapitiExtendedDTO
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response RecapitiExtendedDTO
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "Create a new Recapito", notes = "Create a new Recapito", response = RecapitiExtendedDTO.class, tags={ "recapito", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RecapitiExtendedDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createRecapito(@RequestBody RecapitiExtendedDTO recapito, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "Update an existing Recapito", notes = "", response = RecapitiExtendedDTO.class, tags={ "recapito", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RecapitiExtendedDTO.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Dr not found", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateRecapito(@RequestBody RecapitiExtendedDTO recapito, @QueryParam("indModManuale") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long  indModManuale, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRecapito}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by idRecapito", notes = "Find recapito by idRecapito", response = RecapitiExtendedDTO.class, tags={ "recapito", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RecapitiExtendedDTO.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Dr ID must be an integer and bigger than 0.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A dr with the specified ID was not found.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readRecapitoByPk( @PathParam("idRecapito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idRecapito,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);



}
