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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;


@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiModalitaPagApi {


	@GET
	@Path("/tipi-modalita-pag/{codTipoModalitaPag}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by codTipoModalitaPag", notes = "Find tipo modalita by codTipoModalitaPag", response = TipoModalitaPagDTO.class, tags={ "tipoModalitaPag", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TipoModalitaPagDTO.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Dr ID must be an integer and bigger than 0.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A dr with the specified ID was not found.", response = ErrorDTO.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readTipoModalitaPagByCodTipoModalitaPag( @PathParam("codTipoModalitaPag") String codTipoModalitaPag,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * load All Tipi modalita Pagamenti.
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@Path("/tipi-modalita-pagamenti")
    @GET
    Response loadAllTipiModalitaPagamenti(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
