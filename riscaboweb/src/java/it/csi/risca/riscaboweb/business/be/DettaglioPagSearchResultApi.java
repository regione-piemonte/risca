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

/**
 * The interface  Dettaglio Pag Search Result Api .
 *
 * @author CSI PIEMONTE
 */
@Path("/dettaglio-pag-search-result")
@Produces(MediaType.APPLICATION_JSON)
public interface DettaglioPagSearchResultApi {
	
	
	@GET
	@Path("/idPagamento/{idPagamento}")
    Response getDettaglioPagSearchResultByIdPagamento(@PathParam("idPagamento") String idPagamento,@QueryParam("fruitore") String fruitore,
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
}
