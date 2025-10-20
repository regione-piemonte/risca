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
import javax.ws.rs.DELETE;
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

import it.csi.risca.riscabesrv.dto.LockRiscossioneDTO;

/**
 * The interface LockRiscossioneApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/lock-riscossione")
@Produces(MediaType.APPLICATION_JSON)
public interface LockRiscossioneApi {

	@GET
	@Path("/all")
    Response getAllLockRiscossione(@QueryParam("fruitore") String fruitore,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
    Response getLockRiscossioneByIdRiscossione(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idRiscossione, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveLockRiscossione(@RequestBody LockRiscossioneDTO lockRiscossioneDTO, @QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	
	@DELETE
    Response deleteLockRiscossione(
    		@QueryParam("idRiscossione") Long idRiscossione, @QueryParam("fruitore") String fruitore,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
}
