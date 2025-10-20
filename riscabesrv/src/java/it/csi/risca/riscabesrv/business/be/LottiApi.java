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

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
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

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.dto.LottoDTO;


/**
 * The interface lotti api.
 *
 *
 */
@Path("/lotti")
@Produces(MediaType.APPLICATION_JSON)
public interface LottiApi {


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveLotto(@RequestBody LottoDTO lotto, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

	@Path("/{idElabora}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	Response loadLottiByIdElabora(@PathParam("idElabora") Long idElabora, 
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
