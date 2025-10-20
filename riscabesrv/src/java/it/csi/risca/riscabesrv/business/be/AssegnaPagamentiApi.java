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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.AssegnaPagamentoDTO;

/**
 * The interface AssegnaPagamentiApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/assegna-pagamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface AssegnaPagamentiApi {

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response AssegnaPagamentiPost(@QueryParam("fruitore") String fruitore,
    		@RequestBody AssegnaPagamentoDTO assegnaPagamento, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

}
