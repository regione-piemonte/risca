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

import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;

@Path("/avvisi-pagamento")
@Produces(MediaType.APPLICATION_JSON)
public interface AvvisiPagamentoApi {

	/**
	 * Save avvisi pagamento response.
	 *
	 * @param avvisoPagamento AvvisoPagamentoDTO
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response AvvisoPagamentoDTO
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "Create a new avviso pagamento", notes = "Create a new avviso pagamento", response = AvvisoPagamentoDTO.class, tags = {
			"avvisoPagamento", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = AvvisoPagamentoDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createAvvisoPagamento(@RequestBody AvvisoPagamentoDTO avvisoPagamento,
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{nap}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by nap", notes = "Find avviso pagamento by nap", response = AvvisoPagamentoDTO.class, tags = {
			"avvisoPagamento", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = AvvisoPagamentoDTO.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Dr ID must be an integer and bigger than 0.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A dr with the specified ID was not found.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAvvisoPagamentoByNap(@PathParam("nap") String nap, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	public Response loadAvvisoPagamentoWorkingByIdSpedizione(@QueryParam("idSpedizione")@Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idSpedizione,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/_copy_wrk")
	public Response copyWorkingForAvvisoPagamento(@RequestBody AvvisoPagamentoDTO avvisoPagamentoDTO,
			@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("idSpedizione") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idSpedizione,
			@QueryParam("attore") String attore, @QueryParam("bollettazioneOrdinaria") Boolean bollettazioneOrdinaria,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
