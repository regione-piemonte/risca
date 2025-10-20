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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;

/**
 * The interface Elabora api.
 *
 * @author CSI PIEMONTE
 */
@Path("/parametro-elabora")
@Produces(MediaType.APPLICATION_JSON)
public interface ParametroElaboraApi {
	
    /**
     * Load parametri elaborazione by id_elabora, raggruppamento
     *
     * @param idElabora        idElabora
     * @param raggruppamento      raggruppamento
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@GET
    @Path("")
    Response loadParametroElaboraByElaboraRaggruppamento(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idElabora") String idElabora, 
    		@QueryParam("raggruppamento") String raggruppamento,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveParametroElabora(@RequestBody ParametroElaboraDTO parametroElabora,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateParametroElabora(@RequestBody ParametroElaboraDTO parametroElabora,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
