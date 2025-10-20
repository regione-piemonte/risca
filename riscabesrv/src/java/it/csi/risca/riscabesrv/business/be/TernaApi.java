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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * The interface Terna api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TernaApi {

	@Path("/terna/utenze")
	@GET
	Response loadUtenze(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@Path("/terna/{codUtenza}/energia-prodotta")
	@GET
	Response loadEnergiaProdottaPerAnno(@PathParam("codUtenza") String codUtenza, @QueryParam("anno") String anno,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/terna/{codUtenza}/ricavi")
	@GET
	Response loadRicaviPerAnno(@PathParam("codUtenza") String codUtenza, @QueryParam("anno") String anno,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
