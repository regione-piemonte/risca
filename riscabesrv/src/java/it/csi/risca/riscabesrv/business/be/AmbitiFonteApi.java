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

/**
 * The interface AmbitiFonteApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/ambiti-fonte")
@Produces(MediaType.APPLICATION_JSON)
public interface AmbitiFonteApi {

	/**
	 * Load loadAmbitiFonteByCodFonte .
	 * @param codFonte   codFonte
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/{codFonte}")
	Response loadAmbitiFonteByCodFonte(@PathParam("codFonte") String codFonte, 
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
