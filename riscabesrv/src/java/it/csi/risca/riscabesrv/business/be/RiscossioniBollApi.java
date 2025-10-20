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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/riscossioni-boll")
@Produces(MediaType.APPLICATION_JSON)
public interface RiscossioniBollApi {
	
	@GET
	@Path("/speciale")
    Response loadRiscossioniBollettazioneSpeciale(
    		@QueryParam("codAmbito") String codAmbito, 
    		@QueryParam("dtScadPagamento") String dtScadPagamento, 
    		@QueryParam("tipoElabora") String tipoElabora, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/ordinaria")
    Response loadRiscossioniBollettazioneOrdinaria(
    		@QueryParam("codAmbito") String codAmbito, 
    		@QueryParam("dtScadPagamento") String dtScadPagamento, 
    		@QueryParam("anno") String anno, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

}
